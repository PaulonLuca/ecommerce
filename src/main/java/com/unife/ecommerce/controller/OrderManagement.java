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

public class OrderManagement {

    public static void viewInfoOrder(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        try
        {
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();

            //Recupera id carrello dai cookies
            CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
            Carrello carrello=carrelloDAOCokie.getCookieCart();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento carrello
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

            //Per ogni caricamento
            //1) Crea interfaccia DAO
            //2) Implemanta interfaccia con mysql e fai SELECT * ...
            //3) Inserisci la struttura nel ViewModel
            //4) Popola dinamicamenta la form di inserimento ordine

            //Caricamento indirizzi di spedione
            IndirizzoSpedizioneDAO indSpedDAO=daoFactory.getIndirizzoSpedizioneDAO();
            ArrayList<IndirizzoSpedizione> indirizzi=indSpedDAO.findAllIndirizziSped(loggedUser);

            //Caricamento metodi di spedizione
            SpedizioneDAO spedDAO=daoFactory.getSpedizioneDAO();
            ArrayList<Spedizione> spedizioni=spedDAO.finaAllSpedizioni();

            //Caricamento metodi di pagamento
            TipoPagamentoDAO tipoPagDAO=daoFactory.getTipoPagamentoDAO();
            ArrayList<TipoPagamento> tipiPag=tipoPagDAO.findAllTipiPag();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("carrello", riempito);
            request.setAttribute("selectedProduct",request.getAttribute("selectedProduct"));
            request.setAttribute("spedizioni", spedizioni);
            request.setAttribute("tipiPag", tipiPag);
            request.setAttribute("indirizzi", indirizzi);
            request.setAttribute("viewUrl", "orderManagement/infoOrder");
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    //Visualizza lo storico degli ordini dell'utente loggato
    public static void view(HttpServletRequest request, HttpServletResponse response) {

    }

    //Inserimento di un'ordine nel db e creazione nuovo carrello vuoto sui cookies
    public static void add(HttpServletRequest request, HttpServletResponse response) {

    }
}
