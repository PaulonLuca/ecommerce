package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.SpedizioneDAO;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Spedizione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpedizioneDAOMySQLJDBCImpl implements SpedizioneDAO {

    private final String COUNTER_ID = "id_sped";
    Connection conn;

    public SpedizioneDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ArrayList<Spedizione> finaAllSpedizioni() {
        PreparedStatement ps;
        ArrayList<Spedizione> metodiSpedizione = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Spedizione";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                metodiSpedizione.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metodiSpedizione;
    }

    static Spedizione read(ResultSet rs)
    {
        Spedizione sped = new Spedizione();
        try {
            sped.setIdSped(rs.getLong("id_sped"));
        } catch (SQLException sqle) { }
        try {
            sped.setNomeSped(rs.getString("nome_sped"));
        } catch (SQLException sqle) { }
        try {
            sped.setCosto(rs.getDouble("costo"));
        } catch (SQLException sqle) { }
        try {
            sped.setNumGiorni(rs.getInt("num_giorni"));
        } catch (SQLException sqle) { }
        try {
            sped.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }
        return  sped;
    }


}
