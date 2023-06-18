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

    //Dopo ever riempito il carrello con i prodotti di interesse al click sul pulsante ordina
    //viene visualizzata la form in cui è possibile completare i campi relativi all'ordine:
    //indirizzo (nuovo o già esistente), tipologia di pagemento ( con eventuale nuova carta),
    //tipologia di spedizione
    public static void viewInfoOrder(HttpServletRequest request, HttpServletResponse response) {
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
            request.setAttribute("isAdmin",isAdmin);
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

    //Visualizza lo storico degli ordini dell'utente loggato oppure visualizza gli ordini
    //di tutti gli utenti se l'utente è un amministratore. In modalità amministratore
    //la pagine presentata è interattiva ed è possibile modificare lo stato dell'ordine.
    public static void view(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;
        Carrello riempito=null;

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

            if(!loggedUser.isAdmin())
            {
                //Recupera id carrello dai cookies
                CarrelloDAO carrelloDAOCokie=sessionDAOFactory.getCarrelloDAO();
                Carrello carrello=carrelloDAOCokie.getCookieCart();

                //Caricamento carrello
                CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
                riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);
            }

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento ordini dell'utente loggato, se è un amministratore si caricano gli ordini
            //di tutti gli utenti
            OrdineDAO ordineDAO=daoFactory.getOrdineDAO();

            ArrayList<Ordine> ordini=null;
            ArrayList<Stato> stati=null;
            if(!loggedUser.isAdmin())
                ordini=ordineDAO.findAllOrdiniByUserId(loggedUser.getIdUtente());
            else
            {
                ordini=ordineDAO.findAll();
                StatoDAO statoDAO=daoFactory.getStatoDAO();
                stati=statoDAO.findAll();
            }

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("stati",stati);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("carrello", riempito);
            request.setAttribute("ordini",ordini);
            request.setAttribute("viewUrl", "orderManagement/view");
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

    //Inserimento di un'ordine nel db e creazione nuovo carrello vuoto sui cookies.
    //Al completamento dei campi della form relativa all'ordine quando si preme su conferma:
    //1) Si verifica che tutti i prodotti che si vogliono ordinare in quella quantità siano
    //effettivamente disponibili.
    //2) Se tutti i prodotti sono ok, si recuperano i campi inseriti nella form e si affettuano gli
    //inserimenti nelle verie tabelle di: indirizzo spedizione, spedizione, pagemento e ordine.
    //3) Se alcuni prodotti non sono più disponibili si rimuovono dal
    //carrello e si visualizza l'elenco dei prodotti nel carrello senza tali prodotti.
    //4) Si crea un nuovo carrello vuoto aggiornando il cookie relativo al carrello
    public static void add(HttpServletRequest request, HttpServletResponse response) {
        //Recupera tutti i campi della form e chiama per ogni DAO il metodo di inserimento nel db
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        String applicationMessage="";
        boolean isAdmin;

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
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);
            ArrayList<Prodotto> prodotti=loadProdotti( request);

            //Caricamento carrello
            CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
            Carrello riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);



            //----------------------Verifica se l'ordine può andare a buon fine---------------------
            //Aggiornamento disponibilità prodotti decrementate delle quantità acquistate, se non è possibile
            //acquistare il prodotto perchè qualcuno li ha già acquistati ritornare alla
            //visualizzazione del carrello in cui è stato tolto il/i prodotto/i problematico/i
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            boolean confermaOrdine=true;
            String nonDisponibili="Attenzione uno o più prodotti non disponibili sono stati rimossi dal carrello:\n";
            //Verifica che tutti i prodotti nel carrello hanno una quantià utile al decremento
            for(int i=0;i<riempito.getComposizione().size();i++)
            {
                //Se il prodotto nel carrello ha NON una quantità decrementabile si rimuove dal carrello
                //l'ordine non può essere confermato quindi si presenta nuovamente l'interfaccia carrello
                if(!prodottoDAO.checkQtyDisp(riempito.getComposizione().get(i).getProd().getIdProd(),riempito.getComposizione().get(i).getQty()))
                {
                    nonDisponibili+=riempito.getComposizione().get(i).getProd().getNomeProd()+"\n";
                    carrelloDAOdb.delete(riempito.getIdCart(),riempito.getComposizione().get(i).getProd().getIdProd());
                    confermaOrdine=false;
                }
            }
            //-----------------------------------------------------------------------------------------

            //Se tutti i prodotti hanno una quantità utile al decremento
            if(confermaOrdine)
            {
                applicationMessage="Ordine avvenuto con successo";

                //Decrementa la quantità di ogni prodotto
                for(int i=0;i<riempito.getComposizione().size();i++)
                    prodottoDAO.updateQty(riempito.getComposizione().get(i).getProd().getIdProd(),riempito.getComposizione().get(i).getQty());

                //---------------------------Completamento ordine-------------------------------
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
                Ordine nuovoOrdine=ordineDAO.create(dataOrdine,pag,spedOrdine,statoOrdine,loggedUser,indirizzoSpedizione,false, riempito.getComposizione());

                //Creazione nuovo carrello vuoto nel db e nei cookie
                Carrello vuoto=carrelloDAOdb.create(-1L,loggedUser);
                carrelloDAOCokie.create(vuoto.getIdCart(),loggedUser);

                sessionDAOFactory.commitTransaction();
                daoFactory.commitTransaction();

                //ViewModel
                request.setAttribute("prodottiVetrina", prodottiVetrina);
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("carrello", vuoto);
                request.setAttribute("viewUrl", "homeManagement/view");
            }
            else
            {
                //Alcuni prodotti sono stati eliminati dal carrello
                //Ricarica prodotti se alcuni sono stati eliminati
                prodottiVetrina=loadProdottiVetrina( request);
                prodotti=loadProdotti( request);
                applicationMessage=nonDisponibili;
                //Reload carrello senza gli elementi rimossi
                riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);

                sessionDAOFactory.commitTransaction();
                daoFactory.commitTransaction();

                //ViewModel
                request.setAttribute("prodottiVetrina", prodottiVetrina);
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("carrello", riempito);
                request.setAttribute("viewUrl", "cartManagement/view");

            }
            //View Model comune
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("applicationMessage", applicationMessage);

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

    //Modifica stato dell'ordine
    //Nella visualizzazione di tutti gli ordini nella modalità amministratore viene data la possibilità
    //di modificare lo stato in cui si trova l'ordine attraverso una tendina. Si recupera quindi il nuovo valore
    //e si aggiorna a tale valore il nuovo stato dell'ordine.
    public static void updateOrderState(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;
        Carrello riempito=null;

        try
        {
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
            if(!isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Recupera ordine da modificare e stato da impostare
            Long idStato=Long.parseLong(request.getParameter("newStato"));
            Long idOrdine=Long.parseLong(request.getParameter("selectedOrder"));
            OrdineDAO ordineDAO=daoFactory.getOrdineDAO();
            //Modifica dello stato
            ordineDAO.updateState(idOrdine,idStato);
            //Ricaricemento prodotti con il nuovo stato
            ArrayList<Ordine> ordini=ordineDAO.findAll();
            StatoDAO statoDAO=daoFactory.getStatoDAO();
            //Caricamento di tutti gli stati
            ArrayList<Stato> stati=statoDAO.findAll();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("stati",stati);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("carrello", riempito);
            request.setAttribute("ordini",ordini);
            request.setAttribute("viewUrl", "orderManagement/view");
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
