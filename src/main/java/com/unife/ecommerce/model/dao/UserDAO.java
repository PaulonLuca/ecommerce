package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Utente;

public interface UserDAO
{
    //nella creazione isAdmin, isLocked e inDeleted sono a false di default
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
            boolean deleted);

    public void update(Utente user);

    public void delete(Utente user);

    public Utente findLoggedUser();

    public Utente findByUserId(Long userId);

    public Utente findByUsername(String username);
}
