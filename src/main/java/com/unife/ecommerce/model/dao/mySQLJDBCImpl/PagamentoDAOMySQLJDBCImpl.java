package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.PagamentoDAO;
import com.unife.ecommerce.model.mo.Pagamento;
import com.unife.ecommerce.model.mo.TipoPagamento;

import java.sql.Connection;
import java.sql.Date;

public class PagamentoDAOMySQLJDBCImpl implements PagamentoDAO {

    private final String COUNTER_ID = "id_pag";
    Connection conn;

    public PagamentoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Pagamento createFull(Date data, double totale, String numCarta, String mese, String anno, String cvv, TipoPagamento tipo, boolean deleted) {
        return null;
    }

    @Override
    public Pagamento createSimple(Date data, double totale, TipoPagamento tipo) {
        return null;
    }
}
