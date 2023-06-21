package com.unife.ecommerce.controller;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.*;
import com.unife.ecommerce.services.config.Configuration;
import com.unife.ecommerce.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.unife.ecommerce.controller.HomeManagement.*;

public class CatalogManagement {

    //Permette la visualizzazione delle caratteristiche del prodotto in due modalità
    //User: si visualizza l'immagine ed una tabella con tutte le caratteristiche
    //Admin: si visualizza una pagina con i campi del prodotto all'interno di form,
    // si può operare in modalità modifica
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Carrello carrello=null;
        Carrello riempito=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAmdin=false;
        boolean insertMode=false;

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAmdin=loggedUser.isAdmin();

            //Se utente è loggato e non è un amministratore
            if(loggedUser!=null && !loggedUser.isAdmin())
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

            ArrayList<Fornitore> fornitori=fornitoreDAO.findAllFornitoriForProduct(prodotto.getIdProd());
            boolean inVetrina=prodottoDAO.isInVetrina(Long.parseLong(idProd));
            prodotto.setFornitori(fornitori);
            //Se si clicca sul prodotto da admin si va in modalità di modifica
            //quindi si vedono tutti i fornitori
            if(loggedUser!=null && loggedUser.isAdmin())
                fornitori=fornitoreDAO.findAll();

            if(loggedUser!=null && !loggedUser.isAdmin())
            {
                //Caricamento carrello
                CarrelloDAO carrelloDAOdb=daoFactory.getCarrelloDAO();
                riempito=carrelloDAOdb.loadCarrello(carrello.getIdCart(),fotoPath);
            }

            daoFactory.commitTransaction();

            //ViewModel
            request.setAttribute("isAdmin",isAmdin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("carrello", riempito);
            if(!isAmdin)
                request.setAttribute("viewUrl", "catalogManagement/view");
            else
            {
                //InsertMode=false significa che viene visualizzata la pagina
                //del prodotto in modalità modifica
                request.setAttribute("insertMode",insertMode);
                request.setAttribute("inVetrina",inVetrina);
                request.setAttribute("fornitori", fornitori);
                request.setAttribute("viewUrl", "catalogManagement/infoProdotto");
            }


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

    //Funzionalità richiamata dalla navbar in modalità amministratore per visualizzare
    //la pagina di inserimento prodotto in modalità d'inserimento
    public static void viewInfoProdotto(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAmdin=false;
        boolean insertMode=true;

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAmdin=loggedUser.isAdmin();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();
            //Caricamento informazioni fornitori prodotto
            FornitoreDAO fornitoreDAO=daoFactory.getFornitoreDAO();
            ArrayList<Fornitore> fornitori=fornitoreDAO.findAll();
            daoFactory.commitTransaction();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //ViewModel
            request.setAttribute("inVetrina",false);//da passare per evitare eccezione
            request.setAttribute("fornitori",fornitori);
            request.setAttribute("insertMode",insertMode);
            request.setAttribute("isAdmin",isAmdin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("viewUrl", "catalogManagement/infoProdotto");

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

    //Inserimento/aggiornamento del nuovo prodotto nel db
    //In modalità inserimento:
    //Si recuperano i campi inseriti nella form di creazione nuovo prodotto, si utilizza il metodo
    //create del prodottoDAO per creare un nuovo prodotto
    //In modalità modifica:
    //Si recuperano i campi della form, si modifica il prodotto solo se alcuni campi sono stati modificati,
    //si può aggiungere o togliere il prodotto dalla vetrina
    public static void add(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAdmin=false;

        try
        {
            //Recupera parametri form di input
            //Recupera foto

            boolean insertMode=Boolean.parseBoolean(request.getParameter("insertMode"));
            Long idSelectedProd=0L;
            if(!insertMode)
                idSelectedProd=Long.parseLong(request.getParameter("selectedProd"));
            String fotoPath=request.getServletContext().getRealPath("/uploadedImages");
            String fotoPath1=request.getServletContext().getRealPath("/images");
            String nome=request.getParameter("nome");
            String descrizione=request.getParameter("descrizione");
            int qty_disp=Integer.parseInt(request.getParameter("qty_disp"));
            double prezzo=Double.parseDouble(request.getParameter("prezzo"));
            Long idCategoria=Long.parseLong(request.getParameter("categoria"));
            Long idMarca=Long.parseLong(request.getParameter("marca"));

            String[] fron_str= request.getParameterValues("fornitori");
            ArrayList<Long> fornitori=new ArrayList<>();
            //Fornitori gestiti solo durante gli inserimenti
            if(insertMode)
            {
                for(int i=0;i<fron_str.length;i++)
                    fornitori.add(Long.parseLong(fron_str[i]));
            }


            //se checkbox viene passata come parametro allora è checked
            boolean inVetrina=false;
            String vertina=request.getParameter("vetrina");
            if(vertina!=null)
                inVetrina=true;

            //se checkbox viene passata come parametro allora è checked
            boolean isLocked=false;
            String bloccato=request.getParameter("bloccato");
            if(bloccato!=null)
                isLocked=true;


            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAdmin=loggedUser.isAdmin();
            //Redirect to home page
            if(loggedUser==null || !isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            //Inserimento del prodotto nel db
            MarcaDAO marcaDAO=daoFactory.getMarcaDAO();
            Marca marca=marcaDAO.findMarcaById(idMarca);

            CategoriaDAO categoriaDAO=daoFactory.getCategoriaDAO();
            Categoria categoria=categoriaDAO.findCategoriaById(idCategoria);

            ProdottoDAO prodottoDAO=daoFactory.getProdottoDAO();

            Prodotto p=null;
            //Se si è in insert mode viene inserito il nuovo prodotto
            //Se invece si è in modalità modifica vengono modificati i campi
            if(insertMode)
                p= prodottoDAO.create(-1L,nome,descrizione,qty_disp,prezzo,"uploadedImages",isLocked,marca,categoria,false, fornitori);
            else
            {
                Prodotto oldProd=prodottoDAO.findProdottoById(idSelectedProd,fotoPath);
                Prodotto newProd=new Prodotto();
                newProd.setIdProd(oldProd.getIdProd());
                newProd.setNomeProd(nome);
                newProd.setFotoPath(oldProd.getFotoPath());
                newProd.setFotoProdotto(oldProd.getFotoProdotto());//da riempire
                newProd.setDescr(descrizione);
                newProd.setPrezzo(prezzo);
                newProd.setQtyDisp(qty_disp);
                newProd.setLocked(isLocked);
                newProd.setMarca(marca);
                newProd.setCat(categoria);
                newProd.setDeleted(oldProd.isDeleted());

                //Modifica prodotto esistente solo se alcuni campi sono stati modificati
                if(!oldProd.getNomeProd().equals(newProd.getNomeProd()) || !oldProd.getDescr().equals(newProd.getDescr()) ||
                        oldProd.getQtyDisp()!=newProd.getQtyDisp() || oldProd.getPrezzo()!=newProd.getPrezzo() ||
                        oldProd.isLocked()!=newProd.isLocked() || oldProd.getMarca().getIdMarca()!=newProd.getMarca().getIdMarca()
                        || oldProd.getCat().getIdCat()!=newProd.getCat().getIdCat())
                    prodottoDAO.update(oldProd,newProd);

                //Aggiunge o rimuove il prodotto dalla vetrina
                if(prodottoDAO.isInVetrina(oldProd.getIdProd())!=inVetrina)
                    prodottoDAO.updateVetrina(newProd,inVetrina);
            }

            //Salvataggio foto prodotto
            // uploadFotoProdotto(p.getIdProd(),fotoPath,fotoPath1);

            daoFactory.commitTransaction();

            //Ricarica i campi per la home page
            ArrayList<Prodotto> ricaricati=loadProdottiAdmin(request);
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> ricaricaVetrina=loadProdottiVetrina(request);

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("prodotti",ricaricati);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodottiVetrina",ricaricaVetrina);
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

    //Si richiama al click della funzionalità in modalità amministratore attraverso la navbar.
    //Viene visualizzata la form per inserire una nuova marca oppure una nuova categoria
    public static void viewInsertCatMarca(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAdmin=false;

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAdmin=loggedUser.isAdmin();
            //Redirect to home page
            if(loggedUser==null || !isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            sessionDAOFactory.commitTransaction();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("viewUrl", "catalogManagement/insertCatMarca");

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

    //Recupera la categoria dalla form di inserimento e ne crea una nuova nel db.
    //Viene verificato se il nome era già esistente evitandone quindi il doppio inserimento.
    public static void addCategoria(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAdmin=false;
        String applicationMessage="Nuova categoria inserita";

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAdmin=loggedUser.isAdmin();
            //Redirect to home page
            if(loggedUser==null || !isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            sessionDAOFactory.commitTransaction();

            //Recupera la categoria dai parametri
            String categoria=request.getParameter("categoria");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            //Inserimento della nuova categoria
            try
            {
                CategoriaDAO categoriaDAO=daoFactory.getCategoriaDAO();
                categoriaDAO.create(categoria);
            }catch (DuplicatedObjectException ex)
            {
                applicationMessage="Categoria già presente";
            }

            daoFactory.commitTransaction();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina(request);
            ArrayList<Prodotto> prodotti=loadProdottiAdmin(request);

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotti",prodotti);
            request.setAttribute("prodottiVetrina",prodottiVetrina);
            request.setAttribute("viewUrl", "homeManagement/view");
            request.setAttribute("applicationMessage",applicationMessage);

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

    //Recupera la marca dalla form di inserimento e ne crea una nuova nel db.
    //Viene verificato se il nome era già esistente evitandone quindi il doppio inserimento.
    public static void addMarca(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory=null;
        Utente loggedUser;
        Logger logger = LogService.getApplicationLogger();
        boolean isAdmin=false;
        String applicationMessage="Nuova marca inserita";

        try
        {
            //Recupera utente loggato se presente
            sessionDAOFactory = getSessionDAOFactory(request,response);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            if(loggedUser!=null)
                isAdmin=loggedUser.isAdmin();
            //Redirect to home page
            if(loggedUser==null || !isAdmin)
            {
                request.setAttribute("viewUrl","../index");
                return;
            }

            sessionDAOFactory.commitTransaction();

            //Recupera la categoria dai parametri
            String marca=request.getParameter("marca");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            //Inserimento della nuova marca
            try
            {
                MarcaDAO marcaDAO=daoFactory.getMarcaDAO();
                marcaDAO.create(marca);

            }catch (DuplicatedObjectException doex)
            {
                applicationMessage="Marca già presente";
            }

            daoFactory.commitTransaction();

            //Caricamento marche e categorie
            ArrayList<Marca> marche= loadMarche();
            ArrayList<Categoria> categorie= loadCategorie();
            ArrayList<Prodotto> prodottiVetrina=loadProdottiVetrina(request);
            ArrayList<Prodotto> prodotti=loadProdottiAdmin(request);

            //ViewModel
            request.setAttribute("isAdmin",isAdmin);
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("marche", marche);
            request.setAttribute("categorie", categorie);
            request.setAttribute("prodotti",prodotti);
            request.setAttribute("prodottiVetrina",prodottiVetrina);
            request.setAttribute("viewUrl", "homeManagement/view");
            request.setAttribute("applicationMessage",applicationMessage);

        }
        catch (Exception e) {
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
