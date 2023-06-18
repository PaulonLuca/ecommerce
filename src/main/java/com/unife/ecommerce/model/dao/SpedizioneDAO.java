package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Spedizione;
import java.util.ArrayList;

public interface SpedizioneDAO {

    //Caricamento di tutti i metodi di spedizione
    public ArrayList<Spedizione> finaAllSpedizioni();

    //Caricamento del metodo di spedizione avente l'id specificato
    public Spedizione findSpedizioneById(Long idSped);
}
