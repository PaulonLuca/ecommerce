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


    //Inserimento di un nuovo ordine nel database. Si recupera il contatore relativo all'id ordine
    //dalla tabella counter, si inserisce l'ordine nel db.
    @Override
    public Ordine create(Date dataOrd, Pagamento pag, Spedizione sped, Stato stato, Utente utente, IndirizzoSpedizione ind, boolean deleted , ArrayList<ProdottoQty> composizione) {
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
            ordine.setProdQty(composizione);
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

            //Inserimento composizione ordine
            for(int j=0;j<composizione.size();j++)
            {
                sql="INSERT INTO Composizione VALUES(?,?,?)";
                ps = conn.prepareStatement(sql);
                i=1;
                ps.setLong(i++,composizione.get(j).getProd().getIdProd());
                ps.setLong(i++,ordine.getIdOrd());
                ps.setInt(i++,composizione.get(j).getQty());
                ps.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ordine;
    }

    //Recupera tutti gli ordini di un utente il cui id viene passato dal db
    @Override
    public ArrayList<Ordine> findAllOrdiniByUserId(Long idUtente) {
        PreparedStatement ps;
        ArrayList<Ordine> ordini=new ArrayList<Ordine>();

        //Caricamento ordini dal db
        try
        {
            String sql = "SELECT O.id_ord, O.data_ord,O.deleted,P.id_pag,P.data_pag, P.totale,P.numero_carta,P.mese,P.anno,P.cvv,P.deleted,TP.id_tipo_pag,TP.nome_tipo_pag,TP.deleted," +
                    "S.id_sped,S.nome_sped,S.costo,S.num_giorni,S.deleted, I.id_ind_sped,I.citta,I.via,I.civico,I.cap,I.deleted,ST.id_stato,ST.descr,ST.deleted," +
                    "U.id_utente,U.nome,U.cognome,U.email,U.username,U.psw,U.tel,U.citta,U.via,U.cap,U.civico,U.is_admin,U.is_locked,U.deleted " +
                    "FROM ((((((Ordine AS O INNER JOIN Pagamento AS P ON O.id_pag=P.id_pag) " +
                    "INNER JOIN Tipo_pagamento AS TP ON P.id_tipo_pag=TP.id_tipo_pag) " +
                    "INNER JOIN Spedizione AS S ON S.id_sped=O.id_sped) " +
                    "INNER JOIN Utente AS U ON U.id_utente=O.id_utente) " +
                    "INNER JOIN  Indirizzo_spedizione AS I ON I.id_ind_sped=O.id_ind_sped) " +
                    "INNER JOIN Stato AS ST ON ST.id_stato=O.id_stato) " +
                    "WHERE O.id_utente=?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idUtente);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Ordine o;
                o=read(resultSet);
                //Load composizione ordine
                o.setProdQty(loadComposizione(o.getIdOrd()));

                ordini.add(o);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    //Caricamento ordini di tutti gli utenti dal db
    @Override
    public ArrayList<Ordine> findAll() {

        PreparedStatement ps;
        ArrayList<Ordine> ordini=new ArrayList<Ordine>();

        //Caricamento ordini dal db
        try
        {
            String sql = "SELECT O.id_ord, O.data_ord,O.deleted,P.id_pag,P.data_pag, P.totale,P.numero_carta,P.mese,P.anno,P.cvv,P.deleted,TP.id_tipo_pag,TP.nome_tipo_pag,TP.deleted," +
                    "S.id_sped,S.nome_sped,S.costo,S.num_giorni,S.deleted, I.id_ind_sped,I.citta,I.via,I.civico,I.cap,I.deleted,ST.id_stato,ST.descr,ST.deleted," +
                    "U.id_utente,U.nome,U.cognome,U.email,U.username,U.psw,U.tel,U.citta,U.via,U.cap,U.civico,U.is_admin,U.is_locked,U.deleted " +
                    "FROM ((((((Ordine AS O INNER JOIN Pagamento AS P ON O.id_pag=P.id_pag) " +
                    "INNER JOIN Tipo_pagamento AS TP ON P.id_tipo_pag=TP.id_tipo_pag) " +
                    "INNER JOIN Spedizione AS S ON S.id_sped=O.id_sped) " +
                    "INNER JOIN Utente AS U ON U.id_utente=O.id_utente) " +
                    "INNER JOIN  Indirizzo_spedizione AS I ON I.id_ind_sped=O.id_ind_sped) " +
                    "INNER JOIN Stato AS ST ON ST.id_stato=O.id_stato) ORDER BY O.id_ord";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Ordine o;
                o=read(resultSet);
                //Load composizione ordine
                o.setProdQty(loadComposizione(o.getIdOrd()));
                ordini.add(o);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordini;
    }

    //Modifica stato dell'ordine effettuato
    @Override
    public void updateState(Long idOrdine, Long idStato) {
        PreparedStatement ps;
        try {
            String sql="UPDATE Ordine SET id_stato=? WHERE id_ord=?";
                ps = conn.prepareStatement(sql);
                int i = 1;
                ps.setLong(i++, idStato);
                ps.setLong(i++, idOrdine);
                ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Fuznione di utilità che dato l'id di un ordine carica la lista di tutti gli oggetti ordine
    //che lo compongono
    private ArrayList<ProdottoQty> loadComposizione(Long idOrdine)
    {
        PreparedStatement ps;
        ArrayList<ProdottoQty> composizione=new ArrayList<>();
        try
        {
            String sql = "SELECT P.id_prod,P.nome_prod,P.descr, P.qty_disp, P.prezzo, P.foto_path, P.is_locked,P.id_cat, P.id_marca,P.deleted, C.id_ord,C.qty " +
                    "FROM Prodotto as P INNER JOIN Composizione AS C ON P.id_prod=C.id_prod " +
                    "WHERE C.id_ord=?;";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idOrdine);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                ProdottoQty pqty=new ProdottoQty();
                pqty.setProd(ProdottoDAOMySQLJDBCImpl.read(resultSet));
                pqty.setQty(resultSet.getInt("qty"));
                //Load composizione ordine
                composizione.add(pqty);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return composizione;
    }

    //Lettura dei record relativi al campo ordine per creare l'oggetto ordine
    static Ordine read(ResultSet rs)
    {
        Ordine ordine = new Ordine();
        try {
            ordine.setIdOrd(rs.getLong("id_ord"));
        } catch (SQLException sqle) { }
        try {
            ordine.setDataOrd(rs.getDate("data_ord"));
        } catch (SQLException sqle) { }

        ordine.setUtente(UserDAOMySQLJDBCImpl.read(rs));
        ordine.setIndSped(IndirizzoSpedizioneDAOMySQLJDBCImpl.read(rs));
        ordine.setStato(StatoDAOMySQLJDBCImpl.read(rs));
        ordine.setPag(PagamentoDAOMySQLJDBCImpl.read(rs));
        ordine.getPag().setTipoPag(TipoPagamentoDAOMySQLJDBCImpl.read(rs));
        ordine.setSped(SpedizioneDAOMySQLJDBCImpl.read(rs));

        try {
            ordine.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }

        return  ordine;
    }
}
