package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.OrdineDAO;
import com.unife.ecommerce.model.mo.*;

import java.sql.*;
import java.util.ArrayList;

public class OrdineDAOMySQLJDBCImpl implements OrdineDAO {

    private final String COUNTER_ID = "id_ord";
    Connection conn;

    public OrdineDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Ordine create(Date dataOrd, Pagamento pag, Spedizione sped, Stato stato, Utente utente, IndirizzoSpedizione ind, boolean deleted) {
        Ordine ordine = null;
        try {
            //recupera il valore a cui è arrivato l'id_pag dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            ordine = new Ordine();
            ordine.setIdOrd(resultSet.getLong("counter_value"));
            ordine.setDataOrd(dataOrd);
            ordine.setPag(pag);
            ordine.setSped(sped);
            ordine.setStato(stato);
            ordine.setUtente(utente);
            ordine.setIndSped(ind);
            ordine.setDeleted(deleted);

            resultSet.close();

            sql = "INSERT INTO Ordine VALUES (?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, ordine.getIdOrd());
            ps.setDate(i++, ordine.getDataOrd());
            ps.setLong(i++, ordine.getPag().getIdPaga());
            ps.setLong(i++, ordine.getSped().getIdSped());
            ps.setLong(i++, ordine.getStato().getIdStato());
            ps.setLong(i++, ordine.getUtente().getIdUtente());
            ps.setLong(i++, ordine.getIndSped().getIdIndSped());
            ps.setBoolean(i++, ordine.isDeleted());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ordine;
    }

    @Override
    public ArrayList<Ordine> findAllOrdiniByUserId(Long idUtente) {
        return null;
    }

    @Override
    public ArrayList<Ordine> findAll() {
        return null;
    }

    @Override
    public void updateState(Long idStato) {

    }
}
