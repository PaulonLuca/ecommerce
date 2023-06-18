package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.TipoPagamentoDAO;
import com.unife.ecommerce.model.mo.TipoPagamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TipoPagamentoDAOMySQLJDBCImpl implements TipoPagamentoDAO {

    private final String COUNTER_ID = "id_tipo_pag";
    Connection conn;

    public TipoPagamentoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    //Caricamento di tutte la tipologie  di pagamenti dal db
    @Override
    public ArrayList<TipoPagamento> findAllTipiPag() {
        PreparedStatement ps;
        ArrayList<TipoPagamento> tipi = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Tipo_pagamento";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                tipi.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tipi;
    }

    //Caricamento della tipologia di pagamento specificato come id
    @Override
    public TipoPagamento findTipoPagById(Long idPag) {
        PreparedStatement ps;
        TipoPagamento tipo = null;
        try {
            String sql = "SELECT * FROM Tipo_pagamento WHERE id_tipo_pag=?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idPag);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                tipo=read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tipo;
    }

    //Lettura dei record relativi alla tipologia di pagamento per crearne l'oggetto
    static TipoPagamento read(ResultSet rs)
    {
        TipoPagamento tipo = new TipoPagamento();
        try {
            tipo.setIdTipoPag(rs.getLong("id_tipo_pag"));
        } catch (SQLException sqle) { }
        try {
            tipo.setNomeTipoPag(rs.getString("nome_tipo_pag"));
        } catch (SQLException sqle) { }
        try {
            tipo.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }
        return  tipo;
    }
}
