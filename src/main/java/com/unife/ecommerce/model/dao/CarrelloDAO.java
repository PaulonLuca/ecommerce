package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Carrello;
import com.unife.ecommerce.model.mo.Utente;
import java.sql.SQLException;

public interface CarrelloDAO {

    //Creazione id carrello sui cookies e sul db
    public Carrello create(Long idCart, Utente utente) throws SQLException;

    //eliminazione id carrello dai cookies
    public void destroy();

    //lettura id carrello dai cookies
    public Carrello getCookieCart();

    //aggiunta di un elemento nel carrello del db
    public void add(Long idCart, Long idProd, int qty);

    //rimozione di un elemento dal carrello del db
    public void delete(Long idCart, Long idProd);

    //lettura carrello dal db
    public Carrello loadCarrello(Long idCart,String fotoPath);

}
