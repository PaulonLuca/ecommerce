package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.*;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.unife.ecommerce.controller.HomeManagement.*;

public class UserManagement {

    public static void add(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory daoFactory = null;
        String applicationMessage = "Registrazione avvenuta con successo";
        Logger logger = LogService.getApplicationLogger();

        try
        {
            //recupero parametri dalla request
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String citta = request.getParameter("citta");
            String via = request.getParameter("via");
            String civico = request.getParameter("civico");
            String cap = request.getParameter("cap");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            //Dalla DAOFactory astratta si ottiene la DAOFactory che ritorna i DAO per scrivere e leggere sal db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            UserDAO userDAO = daoFactory.getUserDAO();
            //inserimento utente nel database
            try
            {
                userDAO.create(null, nome, cognome,email,username,password, telefono,citta,via,civico,cap,false,false,false);
            }catch (DuplicatedObjectException ex)
            {   applicationMessage="Username e email già esistenti.";   }

            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",false);//non è amministratore di default alla registrazione
            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "userManagement/registrazione");

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) { }
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) { }
        }
    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;

        try
        {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();
            isAdmin=loggedUser.isAdmin();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento utenti
            UserDAO userDAO=daoFactory.getUserDAO();
            //Caricamento utenti non amministratori
            OrdineDAO ordineDAO=daoFactory.getOrdineDAO();
            ArrayList<Utente> utentiRegistrati=userDAO.findAll(false,loggedUser.getIdUtente());
            //Caricamento ordini per ogni utente registrato
            for(int i=0;i<utentiRegistrati.size();i++)
            {
                Utente current=utentiRegistrati.get(i);
                current.setOrdini(ordineDAO.findAllOrdiniByUserId(current.getIdUtente()));
            }

            //Caricamento utenti amministratori
            ArrayList<Utente> utentiAdmin=userDAO.findAll(true,loggedUser.getIdUtente());

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("utentiRegistrati",utentiRegistrati);
            request.setAttribute("utentiAdmin",utentiAdmin);
            request.setAttribute("viewUrl", "userManagement/view");
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

    public static void update(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LogService.getApplicationLogger();
        DAOFactory sessionDAOFactory=null;
        DAOFactory daoFactory=null;
        boolean isAdmin=false;

        try
        {
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            sessionDAOFactory = getSessionDAOFactory(request,response);
            daoFactory.beginTransaction();
            sessionDAOFactory.beginTransaction();

            //Recupera utente loggato presente
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            Utente loggedUser = sessionUserDAO.findLoggedUser();
            isAdmin=loggedUser.isAdmin();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Recupera ordine da modificare e stato da impostare
            Long idUtente=Long.parseLong(request.getParameter("selectedUser"));
            String field=request.getParameter("modifiedField");
            boolean fieldValue=Boolean.parseBoolean(request.getParameter("modifiedFieldValue"));

            UserDAO utenteDAO=daoFactory.getUserDAO();
            //Modifica campi utente
            utenteDAO.updateField(idUtente,field,fieldValue);

            //Ricaricamento utenti
            UserDAO userDAO=daoFactory.getUserDAO();
            //Caricamento utenti non amministratori
            OrdineDAO ordineDAO=daoFactory.getOrdineDAO();
            ArrayList<Utente> utentiRegistrati=userDAO.findAll(false,loggedUser.getIdUtente());
            //Caricamento ordini per ogni utente registrato
            for(int i=0;i<utentiRegistrati.size();i++)
            {
                Utente current=utentiRegistrati.get(i);
                current.setOrdini(ordineDAO.findAllOrdiniByUserId(current.getIdUtente()));
            }
            //Caricamento utenti amministratori
            ArrayList<Utente> utentiAdmin=userDAO.findAll(true,loggedUser.getIdUtente());

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("utentiRegistrati",utentiRegistrati);
            request.setAttribute("utentiAdmin",utentiAdmin);
            request.setAttribute("viewUrl", "userManagement/view");
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
