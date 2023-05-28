package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.CategoriaDAO;
import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDAOMySQLJDBCImpl implements CategoriaDAO {

    private final String COUNTER_ID = "id_cat";
    Connection conn;

    public CategoriaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ArrayList<Categoria> findAllCategorie() {
        PreparedStatement ps;
        ArrayList<Categoria> categorie = new ArrayList<Categoria>();
        try {
            String sql = " SELECT * FROM Categoria ";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                categorie.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }

    @Override
    public Categoria findCategoriaById(Long idCat) {
        PreparedStatement ps;
        Categoria cat = new Categoria();
        try {
            String sql = " SELECT * FROM Categoria WHERE id_cat=?";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++,idCat);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                cat=read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cat;
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
