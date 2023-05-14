package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.mo.*;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
        //Recupera tutti i campi della form e chiama per ogni DAO il metodo di inserimento nel db
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
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);
            ArrayList<Prodotto> prodotti=loadProdotti( request);

            //Caricamento carrello
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

            //Recupera parametri orderForm
            String radioNewInd=request.getParameter("newInd");
            String selectIndirizzo=request.getParameter("indirizzo");
            String via=request.getParameter("via");
            String civico=request.getParameter("civico");
            String citta=request.getParameter("citta");
            String cap=request.getParameter("cap");
            String spedizione=request.getParameter("spedizione");
            String tipoPag=request.getParameter("tipoPag");
            String numeroCarta=request.getParameter("numeroCarta");
            String mese=request.getParameter("mese");
            String anno=request.getParameter("anno");
            String cvv=request.getParameter("cvv");

            //Inserimento dei vari campi nel db usando i DAO opportuni
            //Inserimento Nuovo indirizzo se presente
            //altrimenti utilizza l'id dell'indirizzo passato nell'insert dell'ordine
            IndirizzoSpedizione indirizzoSpedizione=null;
            IndirizzoSpedizioneDAO indirizzoDAO=daoFactory.getIndirizzoSpedizioneDAO();
            if(selectIndirizzo==null)
                indirizzoSpedizione=indirizzoDAO.create(citta,via,civico,cap,loggedUser);
            else
                indirizzoSpedizione=indirizzoDAO.findIndirizzoById(Long.parseLong(selectIndirizzo));

            //Lettura tipologia spedizione
            SpedizioneDAO spedizioneDAO=daoFactory.getSpedizioneDAO();
            Spedizione spedOrdine=spedizioneDAO.findSpedizioneById(Long.parseLong(spedizione));

            //Inserimento Pagamento
            //Generazione data odierna
            Date dataOrdine= new Date(Calendar.getInstance().getTimeInMillis());
            //Lettura tipo pagamento
            TipoPagamentoDAO tipoPagDAO=daoFactory.getTipoPagamentoDAO();
            TipoPagamento tipoPagamento= tipoPagDAO.findTipoPagById(Long.parseLong(tipoPag));
            //Calcolo totale
            double tot=spedOrdine.getCosto();
            for(int i=0;i<riempito.getComposizione().size();i++)
                tot+=riempito.getComposizione().get(i).getQty()*riempito.getComposizione().get(i).getProd().getPrezzo();

            PagamentoDAO pagDAO=daoFactory.getPagamentoDAO();
            Pagamento pag=null;
            if(tipoPagamento.getIdTipoPag()==2 || tipoPagamento.getIdTipoPag()==3)
                pag=pagDAO.createFull(dataOrdine,tot,numeroCarta,mese,anno,cvv,tipoPagamento,false);
            else
                pag=pagDAO.createSimple(dataOrdine,tot,tipoPagamento,false);


            //Lettura stato
            StatoDAO statoDAO=daoFactory.getStatoDAO();
            Stato statoOrdine=statoDAO.findStatoByDescr("Confermato");

            //Inserimento Ordine con composizione che è contenuta nel carrello, tipologia di spedizione e stato
            OrdineDAO ordineDAO=daoFactory.getOrdineDAO();
            Ordine nuovoOrdine=ordineDAO.create(dataOrdine,pag,spedOrdine,statoOrdine,loggedUser,indirizzoSpedizione,false);


            //Aggiornamento disponibilità prodotti decrementate delle quantità acquistate, se non è possibile
            //acquistare il prodotto perchè qualcuno li ha già acquistati lanciare eccezione e ritornare alla
            //visualizzazione del carrello in cui è stato tolto il prodotto problematico
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            for(int i=0;i<riempito.getComposizione().size();i++)
            {
                //decrementa disponibilità magazzino
            }

            //Creazione nuovo carrello vuoto nel db e nei cookie
            Carrello vuoto=carrelloDAOdb.create(-1L,loggedUser);
            carrelloDAOCokie.create(vuoto.getIdCart(),loggedUser);


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("carrello", riempito);
            request.setAttribute("selectedProduct",request.getAttribute("selectedProduct"));
            request.setAttribute("viewUrl", "homeManagement/view");
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
}