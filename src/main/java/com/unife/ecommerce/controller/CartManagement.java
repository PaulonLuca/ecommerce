package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.mo.*;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.unife.ecommerce.controller.HomeManagement.*;

public class CartManagement {

    //In ogni pagina qualora si provi ad accedere da utente non loggatto o amministratore viene
    //fatta la redirect verso l'index page che riporta l'utente sull'home page

    //Viene recuperato l'id carrello dai cookies e viene caricato il contenuto della tabella del db
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;

        try
        {
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();
            //Redirect to home page
            if(loggedUser==null)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }
            isAdmin=loggedUser.isAdmin();
            if(isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            //Recupera id carrello dai cookies
            CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
            Carrello carrello=carrelloDAOCokie.getCookieCart();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento carrello
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();


            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("carrello", riempito);
            request.setAttribute("selectedProduct",request.getAttribute("selectedProduct"));
            request.setAttribute("viewUrl", "cartManagement/view");
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Si aggiunge il prodotto al carrello nella quantità specificata dopo l'aggiunta si viene reindirizzati
    //nuovamente verso la pagina che visualizza le caratteristiche del prodotto.
    //Con il click su add si aggiorna il valore di quantità sovrascrivendolo ma senza incrementarlo.
    public static void add(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;
        try
        {
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();
            //Redirect to home page
            if(loggedUser==null)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }
            isAdmin=loggedUser.isAdmin();
            if(isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            //Recupera id carrello dai cookies
            CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
            Carrello carrello=carrelloDAOCokie.getCookieCart();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento singolo prodotto
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            FornitoreDAO fornitoreDAO=daoFactory.getFornitoreDAO();
            String idProd=request.getParameter("selectedProduct");
            Prodotto prodotto=prodottoDAO.findProdottoById(Long.parseLong(idProd),fotoPath);
            prodotto.setFornitori(fornitoreDAO.findAllFornitoriForProduct(prodotto.getIdProd()));

            //Inserimento prodotto nel carrello nel db
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            int qty=Integer.parseInt(request.getParameter("qty"));
            carrelloDAOdb.add(carrello.getIdCart(),Long.parseLong(idProd),qty);
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();


            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("carrello", riempito);
            request.setAttribute("selectedProduct",request.getAttribute("selectedProduct"));
            request.setAttribute("viewUrl", "catalogManagement/view");
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Si rimuove il prodotto dal carrello ponendo a 0 la quantità ad esso associata nella
    //composizione del carrello
    public static void remove(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;
        try
        {
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();
            //Redirect to home page
            if(loggedUser==null)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }
            isAdmin=loggedUser.isAdmin();
            if(isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            //Recupera id carrello dai cookies
            CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
            Carrello carrello=carrelloDAOCokie.getCookieCart();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Eliminazione prodotto dal carrello nel db
            String idProd=request.getParameter("selectedProduct");
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            carrelloDAOdb.delete(carrello.getIdCart(),Long.parseLong(idProd));
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();


            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("carrello", riempito);
            request.setAttribute("selectedProduct",request.getAttribute("selectedProduct"));
            request.setAttribute("viewUrl", "cartManagement/view");
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }
}
