package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.StatoDAO;
import com.unife.ecommerce.model.mo.Stato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatoDAOMySQLJDBCImpl implements StatoDAO {

    private final String COUNTER_ID = "id_stato";
    Connection conn;

    public StatoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Stato findStatoByDescr(String descr) {
        PreparedStatement ps;
        Stato stato = new Stato();
        try {
            String sql = "SELECT * FROM Stato WHERE descr=?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, descr);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                stato= read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stato;
    }

    @Override
    public ArrayList<Stato> findAll() {

        PreparedStatement ps;
        ArrayList<Stato> stati=new ArrayList<>();

        try {
            String sql = "SELECT * FROM Stato";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                stati.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stati;
    }

    static Stato read(ResultSet rs)
    {
        Stato stato = new Stato();
        try {
            stato.setIdStato(rs.getLong("id_stato"));
        } catch (SQLException sqle) { }
        try {
            stato.setNomeStato(rs.getString("descr"));
        } catch (SQLException sqle) { }

        try {
            stato.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }

        return  stato;
    }
}
