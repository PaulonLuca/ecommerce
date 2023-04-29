package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.MarcaDAO;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MarcaDAOMySQLJDBCImpl implements MarcaDAO {

    private final String COUNTER_ID = "id_marca";
    Connection conn;

    public MarcaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    static Marca read(ResultSet rs)
    {
        Marca marca = new Marca();
        try {
            marca.setIdMarca(rs.getLong("id_marca"));
        } catch (SQLException sqle) { }
        try {
            marca.setNomeMarca(rs.getString("nome_marca"));
        } catch (SQLException sqle) { }

        return  marca;
    }
}
