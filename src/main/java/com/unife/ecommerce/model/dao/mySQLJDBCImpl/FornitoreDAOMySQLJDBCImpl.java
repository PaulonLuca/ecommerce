package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.FornitoreDAO;
import com.unife.ecommerce.model.mo.Fornitore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FornitoreDAOMySQLJDBCImpl implements FornitoreDAO {

    private final String COUNTER_ID = "id_forn";
    Connection conn;

    public FornitoreDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ArrayList<Fornitore> findAllFornitoriForProduct(Long idProd) {
        PreparedStatement ps;
        ArrayList<Fornitore> fornitori=new ArrayList<>();

        try
        {
            String sql = "SELECT P.id_prod,FF.id_forn, FF.nome_forn " +
                    "FROM (Prodotto AS P INNER JOIN Fornitura AS F ON P.id_prod =F.id_prod) " +
                    "INNER JOIN Fornitore AS FF ON F.id_forn=FF.id_forn " +
                    "WHERE P.id_prod=? " +
                    "ORDER BY id_prod;";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idProd.toString());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                fornitori.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return fornitori;
    }

    static Fornitore read(ResultSet rs) {

        Fornitore forn = new Fornitore();
        try {
            forn.setIdFornitore(rs.getLong("id_forn"));
        } catch (SQLException sqle) {
        }
        try {
            forn.setNomeFornitore(rs.getString("nome_forn"));
        } catch (SQLException sqle) {
        }
        try {
            forn.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) {
        }
        return forn;
    }


}