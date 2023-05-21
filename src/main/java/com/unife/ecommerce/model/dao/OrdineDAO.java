package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.*;

import java.sql.Date;
import java.util.ArrayList;

public interface OrdineDAO {

    public Ordine create(Date dataOrd, Pagamento pag, Spedizione sped, Stato stato, Utente utente, IndirizzoSpedizione ind, boolean deleted, ArrayList<ProdottoQty> composizione);

    public ArrayList<Ordine> findAllOrdiniByUserId(Long idUtente);

    public ArrayList<Ordine> findAll();

    public void updateState(Long idOrdine, Long idStato);

}
