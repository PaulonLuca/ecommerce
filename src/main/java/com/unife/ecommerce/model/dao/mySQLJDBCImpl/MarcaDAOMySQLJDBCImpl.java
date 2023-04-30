package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.MarcaDAO;
import com.unife.ecommerce.model.mo.Marca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MarcaDAOMySQLJDBCImpl implements MarcaDAO {

    private final String COUNTER_ID = "id_marca";
    Connection conn;

    public MarcaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ArrayList<Marca> findAllMarche() {
        PreparedStatement ps;
        ArrayList<Marca> marche = new ArrayList<Marca>();
        try {
            String sql = " SELECT * FROM Marca ";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                marche.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return marche;
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
