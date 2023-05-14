package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.IndirizzoSpedizione;
import com.unife.ecommerce.model.mo.Utente;

import java.util.ArrayList;

public interface IndirizzoSpedizioneDAO {

    public ArrayList<IndirizzoSpedizione> findAllIndirizziSped(Utente utente);

    public IndirizzoSpedizione create(String citta, String via, String civico, String cap, Utente utente);

    public IndirizzoSpedizione findIndirizzoById(Long idInd);
}
