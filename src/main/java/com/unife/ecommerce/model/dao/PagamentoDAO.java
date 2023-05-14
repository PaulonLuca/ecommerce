package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Pagamento;
import com.unife.ecommerce.model.mo.TipoPagamento;

import java.sql.Date;

public interface PagamentoDAO {

    public Pagamento createFull(Date data, double totale, String numCarta, String mese, String anno, String cvv, TipoPagamento tipo, boolean deleted);

    public Pagamento createSimple(Date data, double totale,TipoPagamento tipo, boolean deleted);
}
