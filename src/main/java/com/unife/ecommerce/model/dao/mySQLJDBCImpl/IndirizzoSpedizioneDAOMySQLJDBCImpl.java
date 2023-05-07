package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.IndirizzoSpedizioneDAO;
import com.unife.ecommerce.model.mo.IndirizzoSpedizione;
import com.unife.ecommerce.model.mo.Spedizione;
import com.unife.ecommerce.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IndirizzoSpedizioneDAOMySQLJDBCImpl implements IndirizzoSpedizioneDAO {

    private final String COUNTER_ID = "id_ind_sped";
    Connection conn;

    public IndirizzoSpedizioneDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ArrayList<IndirizzoSpedizione> findAllIndirizziSped(Utente utente) {
        PreparedStatement ps;
        ArrayList<IndirizzoSpedizione> indirizzi = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Indirizzo_spedizone AS IND INNER JOIN Utente AS U ON IND.id_utente=U.id_utente WHERE U.id_utente=?";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setString(i++,utente.getIdUtente().toString());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                indirizzi.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indirizzi;
    }

    @Override
    public IndirizzoSpedizione create(String citta, String via, String civico, String cap, Utente utente) {
        return null;
    }

    static IndirizzoSpedizione read(ResultSet rs)
    {
        IndirizzoSpedizione indSped = new IndirizzoSpedizione();
        try {
            indSped.setIdIndSped(rs.getLong("id_ind_sped"));
        } catch (SQLException sqle) { }
        try {
            indSped.setCitta(rs.getString("citta"));
        } catch (SQLException sqle) { }
        try {
            indSped.setVia(rs.getString("via"));
        } catch (SQLException sqle) { }
        try {
            indSped.setCivico(rs.getString("civico"));
        } catch (SQLException sqle) { }
        try {
            indSped.setCap(rs.getString("cap"));
        } catch (SQLException sqle) { }

        indSped.setUtente(UserDAOMySQLJDBCImpl.read(rs));

        try {
            indSped.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }

        return  indSped;
    }
}
