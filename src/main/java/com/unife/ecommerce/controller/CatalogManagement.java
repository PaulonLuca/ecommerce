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
        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
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
            prodotto.setFornitori(fornitoreDAO.findAllFornitoriForProduct(prodotto.getIdProd()));
            if(loggedUser!=null)
            {
                //Caricamento carrello
                CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
                riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);
            }
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("carrello", riempito);
            request.setAttribute("viewUrl", "catalogManagement/view");

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
}
