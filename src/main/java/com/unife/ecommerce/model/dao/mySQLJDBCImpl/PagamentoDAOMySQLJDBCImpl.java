package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.PagamentoDAO;
import com.unife.ecommerce.model.mo.Pagamento;
import com.unife.ecommerce.model.mo.TipoPagamento;

import java.sql.*;

public class PagamentoDAOMySQLJDBCImpl implements PagamentoDAO {

    private final String COUNTER_ID = "id_pag";
    Connection conn;

    public PagamentoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Pagamento createFull(Date data, double totale, String numCarta, String mese, String anno, String cvv, TipoPagamento tipo, boolean deleted) {
        Pagamento pag = null;
        try {
            //recupera il valore a cui è arrivato l'id_pag dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            pag = new Pagamento();
            pag.setIdPaga(resultSet.getLong("counter_value"));
            pag.setDataPag(data);
            pag.setTotale(totale);
            pag.setNumeroCarta(numCarta);
            pag.setMese(Integer.parseInt(mese));
            pag.setAnno(Integer.parseInt(anno));
            pag.setCvv(Integer.parseInt(cvv));
            pag.setTipoPag(tipo);
            pag.setDeleted(deleted);

            resultSet.close();

            sql = "INSERT INTO Pagamento VALUES (?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, pag.getIdPaga());
            ps.setDate(i++, pag.getDataPag());
            ps.setDouble(i++, pag.getTotale());
            ps.setString(i++, pag.getNumeroCarta());
            ps.setInt(i++, pag.getMese());
            ps.setInt(i++, pag.getAnno());
            ps.setInt(i++, pag.getCvv());
            ps.setLong(i++,pag.getTipoPag().getIdTipoPag());
            ps.setBoolean(i++, pag.isDeleted());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return pag;
    }

    @Override
    public Pagamento createSimple(Date data, double totale, TipoPagamento tipo, boolean deleted) {
        Pagamento pag = null;
        try {
            //recupera il valore a cui è arrivato l'id_pag dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            pag = new Pagamento();
            pag.setIdPaga(resultSet.getLong("counter_value"));
            pag.setDataPag(data);
            pag.setTotale(totale);
            pag.setTipoPag(tipo);
            pag.setDeleted(deleted);

            resultSet.close();

            sql = "INSERT INTO Pagamento(id_pag,data_pag,totale,id_tipo_pag,deleted) VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, pag.getIdPaga());
            ps.setDate(i++, pag.getDataPag());
            ps.setDouble(i++, pag.getTotale());
            ps.setLong(i++,pag.getTipoPag().getIdTipoPag());
            ps.setBoolean(i++, pag.isDeleted());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return pag;
    }

    static Pagamento read(ResultSet rs)
    {
        Pagamento pag = new Pagamento();
        try {
            pag.setIdPaga(rs.getLong("id_pag"));
        } catch (SQLException sqle) { }
        try {
            pag.setDataPag(rs.getDate("data_pag"));
        } catch (SQLException sqle) { }
        try {
            pag.setTotale(rs.getDouble("totale"));
        } catch (SQLException sqle) { }
        try {
            pag.setNumeroCarta(rs.getString("numero_carta"));
        } catch (SQLException sqle) { }
        try {
            pag.setMese(rs.getInt("mese"));
        } catch (SQLException sqle) { }
        try {
            pag.setAnno(rs.getInt("anno"));
        } catch (SQLException sqle) { }
        try {
            pag.setCvv(rs.getInt("data_pag"));
        } catch (SQLException sqle) { }
        try {
            pag.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) { }
        return  pag;
    }
}
