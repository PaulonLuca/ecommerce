package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.DAOFactory;
import com.unife.ecommerce.model.dao.UserDAO;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "userManagement/registrazione");

        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
