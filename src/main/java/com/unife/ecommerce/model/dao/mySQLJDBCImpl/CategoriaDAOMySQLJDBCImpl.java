package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.CategoriaDAO;
import com.unife.ecommerce.model.mo.Categoria;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDAOMySQLJDBCImpl implements CategoriaDAO {

    private final String COUNTER_ID = "id_cat";
    Connection conn;

    public CategoriaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    static Categoria read(ResultSet rs)
    {
        Categoria cat = new Categoria();
        try {
            cat.setIdCat(rs.getLong("id_cat"));
        } catch (SQLException sqle) { }
        try {
            cat.setNomeCat(rs.getString("nome_cat"));
        } catch (SQLException sqle) { }

        return cat;
    }
}
