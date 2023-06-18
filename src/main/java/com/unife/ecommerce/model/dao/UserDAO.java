package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.Utente;
import java.util.ArrayList;

//Interfaccia implementata sui Cookies ed in MySQL
public interface UserDAO
{
    //nella creazione isAdmin, isLocked e inDeleted sono a false di default
    //Crazione di un nuovo utente nel db o nei cookies
    public Utente create(
            Long idUtente,
            String nome,
            String cognome,
            String email,
            String username,
            String psw,
            String tel,
            String citta,
            String via,
            String civico,
            String cap,
            boolean isAdmin,
            boolean isLocked,
            boolean deleted) throws DuplicatedObjectException;

    //Aggiornamento dell'utente nei cookies
    public void update(Utente user);

    //Eliminazione dell'utente dai cookies
    public void delete(Utente user);

    //Caricamento dell'utente loggato dal db
    public Utente findLoggedUser();

    //Caricamento dell'utente dal db dato l'id
    public Utente findByUserId(Long userId);

    //Caricamento dell'utente dal db dato lo username
    public Utente findByUsername(String username);

    //Caricamento di tutti gli utenti dal db
    public ArrayList<Utente> findAll(boolean isAdmin, Long idLogged);

    //Aggiornamento di un campo dell'utente (is_locked, is_admin) dal pannello di controllo dell'admin
    public void updateField(Long idUtente, String field, boolean value);

}
