package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Stato;
import java.util.ArrayList;

public interface StatoDAO {

    //Caricamento oggetto stato in base alla descrizione
    public Stato findStatoByDescr(String descr);

    //Cariacamento di tutti gli stati possibili
    public ArrayList<Stato> findAll();
}
