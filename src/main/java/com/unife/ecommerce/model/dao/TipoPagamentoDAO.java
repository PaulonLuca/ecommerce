package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.TipoPagamento;

import java.util.ArrayList;

public interface TipoPagamentoDAO {

    public ArrayList<TipoPagamento> findAllTipiPag();

    public TipoPagamento findTipoPagById(Long idPag);
}
