package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;
import com.unife.ecommerce.model.mo.Utente;
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

    static int numProdottiPagina=6;

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            sessionDAOFactory.commitTransaction();

            //Caricamento prodotti, marche e categorie
            ArrayList<Prodotto> prodotti=loadProdotti( request);
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
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

    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        String applicationMessage = "Benvenuto, inizia ad acquistare i tuoi prodotti";
        Logger logger = LogService.getApplicationLogger();

        try {
            //Caricamento prodotti, marche e categorie
            ArrayList<Prodotto> prodotti=loadProdotti( request);
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina( request);

            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sui cookie
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();
            //Da cookieDAO factory si ootiene lo UserDAO per scrivere e leggere dai cookie
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            //loggedUser = sessionUserDAO.findLoggedUser();

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
            } else {
                loggedUser = sessionUserDAO.create(user.getIdUtente(),user.getNome(),user.getCognome(),user.getEmail(),user.getUsername(),
                        null,null,null,null,null,null,user.isAdmin(),false,false);//<--hard coded
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina", prodottiVetrina);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

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
            //eliminazione cookie con l'utente
            sessionUserDAO.delete(null);
            sessionDAOFactory.commitTransaction();

            //ViewModel
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

    public static void registrationView(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("loggedOn",false);
        request.setAttribute("loggedUser", null);
        request.setAttribute("viewUrl", "userManagement/registrazione");
    }

    protected static DAOFactory getSessionDAOFactory(HttpServletRequest request, HttpServletResponse response){
        Map sessionFactoryParameters=new HashMap<String,Object>();
        sessionFactoryParameters.put("request",request);
        sessionFactoryParameters.put("response",response);
        DAOFactory sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
        return  sessionDAOFactory;
    }

    protected static ArrayList<Prodotto> loadProdotti( HttpServletRequest request){
        //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
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

        //Paginazione prodotti
        /*int paginationIndex=1;
        if(request.getAttribute("paginationIndex") !=null)
            paginationIndex=Integer.parseInt((String)request.getAttribute("paginationIndex"));

        ArrayList<Prodotto> prodottiPagina=new ArrayList<>();
        int limit=0;
        if(numProdottiPagina>(prodotti.size()- numProdottiPagina *paginationIndex))
            limit=prodotti.size()- numProdottiPagina *paginationIndex;
        else
            limit=numProdottiPagina * paginationIndex;

        for(int i=(paginationIndex-1)*numProdottiPagina;i<limit ;i++)
            prodottiPagina.add(prodotti.get(i));



        int numPagine=(Math.round(prodotti.size()/numProdottiPagina))+1;
        request.setAttribute("numPagine", numPagine);*/

        daoFactory.commitTransaction();

        return prodotti;
    }

    protected static ArrayList<Prodotto> loadProdottiVetrina(HttpServletRequest request){
        //Recupera root path della cartella foto
        String fotoPath=request.getServletContext().getRealPath("/uploadedImages");

        //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        daoFactory.beginTransaction();
        ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
        ArrayList<Prodotto> prodottiVetrina=prodottoDAO.findProdottiVetrina(1L,fotoPath);
        daoFactory.commitTransaction();
        return prodottiVetrina;
    }

    protected static ArrayList<Marca> loadMarche(){
        //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        daoFactory.beginTransaction();
        MarcaDAO marcaDAO=daoFactory.getMarcaDAO();
        ArrayList<Marca> marche=marcaDAO.findAllMarche();
        daoFactory.commitTransaction();
        return marche;
    }

    protected static ArrayList<Categoria> loadCategorie(){
        //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        daoFactory.beginTransaction();
        CategoriaDAO categoriaDAO=daoFactory.getCategoriaDAO();
        ArrayList<Categoria> categorie=categoriaDAO.findAllCategorie();
        daoFactory.commitTransaction();
        return categorie;
    }
}
