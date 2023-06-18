package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.TipoPagamento;
import java.util.ArrayList;

public interface TipoPagamentoDAO {

    //Caricamenti di tutte le tipologie di pagamento
    public ArrayList<TipoPagamento> findAllTipiPag();

    //Caricamento della tipologia di pagemento avente l'id passato
    public TipoPagamento findTipoPagById(Long idPag);
}
