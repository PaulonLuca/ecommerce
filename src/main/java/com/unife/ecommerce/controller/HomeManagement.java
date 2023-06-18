package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.mo.*;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeManagement {

    //Visualizza i prodotti della home page.
    //Se amministratore: non si recupera il carrello e non si riempie con i prodotti, ma vengono visualizzati
    //anche i prodotti bloccati e con qty=0
    //Se utente normale: si recupera id carrello, si caricano i prodotti e si visualizzano solo prodotti
    //non bloccati e con qty>0
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        Carrello riempito=null;
        boolean isAdmin=false;
        try
        {
            //Recupera utente loggato se presente dai cookie
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            sessionDAOFactory.commitTransaction();

            //Se utente è loggato recupero anche il carrello associato all'utente dal db

            if(loggedUser!=null )
            {
                //Se utente è amministratore non ha un carrello
                if(!loggedUser.isAdmin())
                {
                    //Recupera id carrello dai cookies
                    CarrelloDAO carrelloDAOCookie=sessionDAOFactory.getCarrelloDAO();
                    Carrello carrello=carrelloDAOCookie.getCookieCart();
                    daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
                    daoFactory.beginTransaction();
                    CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
                    String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
                    riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);
                    daoFactory.commitTransaction();
                }
                else
                    isAdmin=loggedUser.isAdmin();
            }

            //Caricamento prodotti, marche e categorie
            ArrayList<Prodotto> prodotti=null;
            if(isAdmin)
                prodotti=loadProdottiAdmin( request);
            else
                prodotti=loadProdotti(request);

            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);


            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            if(loggedUser!=null) request.setAttribute("carrello", riempito);
            request.setAttribute("viewUrl", "homeManagement/view");

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

    //Gestione del logon dell'utente:
    //Va a buon fine: si visualizza il nome utente nella navbar
    //Errore nelle credenziali: si visualizza un alert in cui si avvisa l'utente di tale errore
    //Utente locked: se l'utente che prova ad effettuare il logon è bloccato questo non
    //riesce ad effettuare il logon
    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser=null;
        Carrello carrello=null;
        String applicationMessage = "Benvenuto, inizia ad acquistare i tuoi prodotti";
        Logger logger = LogService.getApplicationLogger();
        boolean isAdmin=false;

        try
        {
            //Caricamento prodotti, marche e categorie
            ArrayList<Prodotto> prodotti=null;
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);

            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sui cookie
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();
            //Da cookieDAO factory si ottiene lo UserDAO per scrivere e leggere dai cookie
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();

            //Recupero dati utente dal db
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            UserDAO userDAO = daoFactory.getUserDAO();
            //recupera l'utente dal db attraverso lo username
            Utente user = userDAO.findByUsername(username);

            //verifica se la password è corretta
            if (user == null || !user.getPsw().equals(password)) {
                sessionUserDAO.delete(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
                prodotti=loadProdotti(request);
            } else {

                if(!user.isLocked())
                {
                    //Logon avvenuto con successo, creazione utente sui cookies
                    loggedUser = sessionUserDAO.create(user.getIdUtente(),user.getNome(),user.getCognome(),user.getEmail(),user.getUsername(),
                            null,null,null,null,null,null,user.isAdmin(),false,false);//<--hard coded

                    //Carrello presente solo se l'utente non è un amministratore
                    if(!loggedUser.isAdmin())
                    {
                        prodotti=loadProdotti(request);
                        //Creazione carrello
                        CarrelloDAO sessionCartDAO=sessionDAOFactory.getCarrelloDAO();
                        CarrelloDAO carrelloDAO=daoFactory.getCarrelloDAO();
                        //Creazione carrello nel db
                        carrello= carrelloDAO.create(-1L,loggedUser);
                        //creazione carrello nei cookies
                        sessionCartDAO.create(carrello.getIdCart(),loggedUser);
                    }
                    else
                    {
                        prodotti=loadProdottiAdmin(request);
                        applicationMessage = "Benvenuto, inizia a gestire l'e-commerce e gli altri utenti";
                    }

                    //verifica se l'utente è anche amministratore
                    isAdmin=user.isAdmin();
                }
                else
                {
                    applicationMessage="Impossibile fare logon. L'utente risulta bloccato.";
                    prodotti=loadProdotti(request);
                }

            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            request.setAttribute("carrello", carrello);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) { }
        }

    }

    //Eliminazione del cookie utente dai cookies e anche di quello relativo al carrello
    //se l'utente non è amministratore
    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        String applicationMessage = "Logout avvenuto con successo";
        Logger logger = LogService.getApplicationLogger();

        try
        {
            //Caricamento prodotti, marche e categorie
            ArrayList<Prodotto> prodotti=loadProdotti( request);
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);

            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser=sessionUserDAO.findLoggedUser();

            //eliminazione cookie con l'utente
            sessionUserDAO.delete(null);

            //Eliminazione carrello dai cookies, nel db rimane lo storico
            //Se l'utente non è amministratore
            if(!loggedUser.isAdmin()) {
                CarrelloDAO sessionCartDAO = sessionDAOFactory.getCarrelloDAO();
                sessionCartDAO.destroy();
            }

            sessionDAOFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",false);
            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Funzionalità richiamata navbar da soli utenti non loggati, viene data la possibilità
    //di registrarsi all'ecommerce
    public static void registrationView(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("loggedOn",false);
        request.setAttribute("loggedUser", null);
        request.setAttribute("viewUrl", "userManagement/registrazione");
    }

    //Funzione di utilità per recuperare la sessionDAOFactory
    protected static DAOFactory getSessionDAOFactory(HttpServletRequest request, HttpServletResponse response){
        Map sessionFactoryParameters=new HashMap<String,Object>();
        sessionFactoryParameters.put("request",request);
        sessionFactoryParameters.put("response",response);
        DAOFactory sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
        return  sessionDAOFactory;
    }

    //Funzione di utilità per caricare la lista di tutti i prodotti dal db
    protected static ArrayList<Prodotto> loadProdotti( HttpServletRequest request){
        DAOFactory daoFactory=null;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();

            //Recupera root path della cartella foto
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            //Recupero parametri di selezione, se non sono presenti valgono null, se invece è stato fatto click su uno
            //solo dei tre link uno è valorizzato gli altri sono stringhe vuote
            String idCat=request.getParameter("selectedCat");
            String idMarca=request.getParameter("selectedMarca");
            String searchString=request.getParameter("searchString");
            ArrayList<Prodotto> prodotti=prodottoDAO.findAllProdotti(fotoPath,idCat,idMarca,searchString);

            daoFactory.commitTransaction();
            return prodotti;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Funziona di utilità per caricare tutti i prodotti del db, in questo caso però si considerano
    //anche prodotti bloccati e con qty>0
    protected static ArrayList<Prodotto> loadProdottiAdmin( HttpServletRequest request){
        DAOFactory daoFactory=null;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            String idCat=request.getParameter("selectedCat");
            String idMarca=request.getParameter("selectedMarca");
            String searchString=request.getParameter("searchString");

            ArrayList<Prodotto> prodotti=prodottoDAO.findAllProdottiAdmin(fotoPath,idCat,idMarca,searchString);

            daoFactory.commitTransaction();
            return prodotti;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Funzione di utilità per caricare nel carosello i prodotti che sono in vetrina
    protected static ArrayList<Prodotto> loadProdottiVetrina(HttpServletRequest request){
        DAOFactory daoFactory=null;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Recupera root path della cartella foto
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            ArrayList<Prodotto> prodottiVetrina=prodottoDAO.findProdottiVetrina(1L,fotoPath);
            daoFactory.commitTransaction();
            return prodottiVetrina;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }

    }

    //Funzione di utilità per caricare le marche dal db in una lista
    protected static ArrayList<Marca> loadMarche(){
        DAOFactory daoFactory=null;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            MarcaDAO marcaDAO=daoFactory.getMarcaDAO();
            ArrayList<Marca> marche=marcaDAO.findAllMarche();
            daoFactory.commitTransaction();
            return marche;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    //Funzione di utilità per caricare le categorie dal db in una lista
    protected static ArrayList<Categoria> loadCategorie(){
        DAOFactory daoFactory=null;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            CategoriaDAO categoriaDAO=daoFactory.getCategoriaDAO();
            ArrayList<Categoria> categorie=categoriaDAO.findAllCategorie();
            daoFactory.commitTransaction();
            return categorie;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory!=null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);

        } finally {
            try {
                if(daoFactory!=null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }
}
