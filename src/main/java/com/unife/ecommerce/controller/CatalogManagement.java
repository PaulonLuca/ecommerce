package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.DAOFactory;
import com.unife.ecommerce.model.dao.FornitoreDAO;
import com.unife.ecommerce.model.dao.ProdottoDAO;
import com.unife.ecommerce.model.dao.UserDAO;
import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;
import com.unife.ecommerce.model.mo.Utente;
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

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //Caricamento informazioni singolo prodotto
            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();
            //Caricamento informazioni fornitori prodotto
            FornitoreDAO fornitoreDAO=daoFactory.getFornitoreDAO();
            //Recupera root path della cartella foto
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            String idProd=request.getParameter("selectedProduct");
            Prodotto prodotto=prodottoDAO.findProdottoById(Long.parseLong(idProd),fotoPath);
            prodotto.setFornitori(fornitoreDAO.findAllFornitoriForProduct(prodotto.getIdProd()));
            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "catalogManagement/view");

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
}
