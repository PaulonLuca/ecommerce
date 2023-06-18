package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.IndirizzoSpedizione;
import com.unife.ecommerce.model.mo.Utente;
import java.util.ArrayList;

public interface IndirizzoSpedizioneDAO {

    //Caricamento di tutti gli indirizzi di spedizione di un utente dal db
    public ArrayList<IndirizzoSpedizione> findAllIndirizziSped(Utente utente);

    //Inserimento di un nuovo indirizzo di spedizione nel db per un certo utente
    public IndirizzoSpedizione create(String citta, String via, String civico, String cap, Utente utente);

    //Caricamento dell'indirizzo di spedizione evente l'id passato
    public IndirizzoSpedizione findIndirizzoById(Long idInd);
}
