package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.DAOFactory;
import com.unife.ecommerce.model.dao.ProdottoDAO;
import com.unife.ecommerce.model.dao.UserDAO;
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

            //Caricamento prodotti
            ArrayList<Prodotto> prodotti=loadProdotti(null, request);

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti", prodotti);
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
            //Caricamento prodotti
            ArrayList<Prodotto> prodotti=loadProdotti(null,request);

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

        try {
            //Caricamento prodotti
            ArrayList<Prodotto> prodotti=loadProdotti(null,request);

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

    private static DAOFactory getSessionDAOFactory(HttpServletRequest request, HttpServletResponse response){
        Map sessionFactoryParameters=new HashMap<String,Object>();
        sessionFactoryParameters.put("request",request);
        sessionFactoryParameters.put("response",response);
        DAOFactory sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
        return  sessionDAOFactory;
    }

    private static ArrayList<Prodotto> loadProdotti(String searchString, HttpServletRequest request){
        //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
        DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
        daoFactory.beginTransaction();
        ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
        ArrayList<Prodotto> prodotti=prodottoDAO.findAllProdotti(searchString,request);
        daoFactory.commitTransaction();
        return prodotti;
    }
}
