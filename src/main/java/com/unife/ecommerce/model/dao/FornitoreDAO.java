package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Fornitore;
import java.util.ArrayList;

public interface FornitoreDAO {

    //Caricamento di tutti i fornitori relativi ad un prodotto dal db
    public ArrayList<Fornitore> findAllFornitoriForProduct(Long idProd);

    //Craicamento di tutti i fornitori disponibili dal db
    public ArrayList<Fornitore> findAll();
}
