package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.*;
import java.sql.Date;
import java.util.ArrayList;

public interface OrdineDAO {

    //Inserimento di un nuovo ordine nel db
    public Ordine create(Date dataOrd, Pagamento pag, Spedizione sped, Stato stato, Utente utente, IndirizzoSpedizione ind, boolean deleted, ArrayList<ProdottoQty> composizione);

    //Caricamento di tutti gli ordini relativi all'id dell'utente passato
    public ArrayList<Ordine> findAllOrdiniByUserId(Long idUtente);

    //Caricamento di tutti gli ordini dal db
    public ArrayList<Ordine> findAll();

    //Aggiornamento dello stato dell'ordine passato con il nuovo stato
    public void updateState(Long idOrdine, Long idStato);

}
