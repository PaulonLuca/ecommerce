package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.IndirizzoSpedizioneDAO;
import com.unife.ecommerce.model.mo.Carrello;
import com.unife.ecommerce.model.mo.IndirizzoSpedizione;
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
            String sql = "SELECT * FROM Indirizzo_spedizione AS IND INNER JOIN Utente AS U ON IND.id_utente=U.id_utente WHERE U.id_utente=?";
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
        IndirizzoSpedizione indSped = null;
        try {
            //recupera il valore a cui è arrivato l'id_utente dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            indSped = new IndirizzoSpedizione();
            indSped.setIdIndSped(resultSet.getLong("counter_value"));
            indSped.setUtente(utente);
            indSped.setCitta(citta);
            indSped.setVia(via);
            indSped.setCap(cap);
            indSped.setCivico(civico);
            indSped.setDeleted(false);

            resultSet.close();

            sql = "INSERT INTO Indirizzo_spedizione VALUES (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, indSped.getIdIndSped());
            ps.setString(i++, indSped.getCitta());
            ps.setString(i++, indSped.getVia());
            ps.setString(i++, indSped.getCivico());
            ps.setString(i++, indSped.getCap());
            ps.setLong(i++, utente.getIdUtente());
            ps.setBoolean(i++, indSped.isDeleted());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return indSped;
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
