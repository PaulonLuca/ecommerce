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

public class CatalogManagement {

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Carrello carrello=null;
        Carrello riempito=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAmdin=false;
        boolean insertMode=false;

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAmdin=loggedUser.isAdmin();

            //Se utente è loggato e non è un amministratore
            if(loggedUser!=null && !loggedUser.isAdmin())
            {
                //Recupera id carrello dai cookies
                CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
                carrello=carrelloDAOCokie.getCookieCart();
            }

            sessionDAOFactory.commitTransaction();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento informazioni singolo prodotto
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            //Caricamento informazioni fornitori prodotto
            FornitoreDAO fornitoreDAO=daoFactory.getFornitoreDAO();
            //Recupera root path della cartella foto
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            String idProd=request.getParameter("selectedProduct");
            Prodotto prodotto=prodottoDAO.findProdottoById(Long.parseLong(idProd),fotoPath);
            ArrayList<Fornitore> fornitori=fornitoreDAO.findAllFornitoriForProduct(prodotto.getIdProd());
            boolean inVetrina=prodottoDAO.isInVetrina(Long.parseLong(idProd));
            prodotto.setFornitori(fornitori);
            if(loggedUser!=null && !loggedUser.isAdmin())
            {
                //Caricamento carrello
                CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
                riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);
            }
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAmdin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("carrello", riempito);
            if(!isAmdin)
                request.setAttribute("viewUrl", "catalogManagement/view");
            else
            {
                //InsertMode=false significa che viene visualizzata la pagina
                //del prodotto in modalità modifica
                request.setAttribute("insertMode",insertMode);
                request.setAttribute("inVetrina",inVetrina);
                request.setAttribute("fornitori", fornitori);
                request.setAttribute("viewUrl", "catalogManagement/infoProdotto");
            }


        } catch (Exception e) {
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

    public static void viewInfoProdotto(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAmdin=false;
        boolean insertMode=true;

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAmdin=loggedUser.isAdmin();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            //Caricamento informazioni fornitori prodotto
            FornitoreDAO fornitoreDAO=daoFactory.getFornitoreDAO();
            ArrayList<Fornitore> fornitori=fornitoreDAO.findAll();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //ViewModel
            request.setAttribute("inVetrina",false);//da passare per evitare eccezione
            request.setAttribute("fornitori",fornitori);
            request.setAttribute("insertMode",insertMode);
            request.setAttribute("isAdmin",isAmdin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("viewUrl", "catalogManagement/infoProdotto");

        } catch (Exception e) {
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

    //Modifica dei campi prodotto nel db
    public static void update(HttpServletRequest request, HttpServletResponse response) {

    }

    //Inserimento del nuovo prodotto nel db
    public static void add(HttpServletRequest request, HttpServletResponse response) {

    }
}
