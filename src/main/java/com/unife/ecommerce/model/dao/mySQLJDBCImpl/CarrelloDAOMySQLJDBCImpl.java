package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.CarrelloDAO;
import com.unife.ecommerce.model.mo.Carrello;
import com.unife.ecommerce.model.mo.ProdottoQty;
import com.unife.ecommerce.model.mo.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarrelloDAOMySQLJDBCImpl implements CarrelloDAO {

    private final String COUNTER_ID = "id_cart";
    Connection conn;

    public CarrelloDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    //Creazione di un nuovo carrello nel database. Si recupera il punto a cui è errivato il contatore della
    //tabella di utilità counter e si inserisce il nuovo carrello nel db.
    @Override
    public Carrello create(Long idCart, Utente utente) throws SQLException {
        Carrello carrello=null;
        try
        {
            //recupera il valore a cui è arrivato l'id_utente dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            carrello=new Carrello();
            carrello.setIdCart(resultSet.getLong("counter_value"));
            carrello.setUtente(utente);
            carrello.setComposizione(new ArrayList<>());
            carrello.setDeleted(false);

            resultSet.close();

            sql = "INSERT INTO Carrello VALUES (?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, carrello.getIdCart());
            ps.setLong(i++, carrello.getUtente().getIdUtente());
            ps.setBoolean(i++, carrello.isDeleted());
            ps.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
        return carrello;
    }

    @Override
    public void destroy() {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public Carrello getCookieCart() {throw new UnsupportedOperationException("Not supported yet.");}

    //Inserimento prodotto nel carrello.
    //Se non presente: inserimento del record in contenuto_carrello
    //Se già presente: si aggiorna in record in contenuto carrello
    @Override
    public void add(Long idCart, Long idProd, int qty) {
        PreparedStatement ps;
        try {
            //verifica se il prodotto è già presente nel carrello
            String sql = " SELECT * FROM Contenuto_carrello WHERE id_cart = ? AND id_prod = ?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idCart);
            ps.setLong(i++, idProd);
            ResultSet resultSet = ps.executeQuery();
            boolean exist = resultSet.next();
            resultSet.close();
            //se già presente nel carrello si aggiorna la quantità
            if (exist) {
                sql="UPDATE Contenuto_carrello SET qty=? WHERE id_cart=? AND id_prod=?";
                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setInt(i++, qty);
                ps.setLong(i++, idCart);
                ps.setLong(i++, idProd);
                ps.executeUpdate();
            }
            else
            {
                //viene inserito nel carrello
                sql="INSERT INTO Contenuto_carrello VALUES(?,?,?)";
                ps = conn.prepareStatement(sql);
                i = 1;
                ps.setLong(i++, idProd);
                ps.setLong(i++, idCart);
                ps.setInt(i++, qty);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Eliminazione prodotto dal carrello.
    //Si pone a 0 la quantità dal prodotto nel carrello, si è deciso per tale approccio
    //poichè si possono avere informazioni sul tracciamento dell'utente.
    @Override
    public void delete(Long idCart, Long idProd) {
        PreparedStatement ps;
        try
        {
            String sql="UPDATE Contenuto_carrello SET qty=0 WHERE id_cart=? AND id_prod=?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idCart);
            ps.setLong(i++, idProd);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Si recuperano tutti i prodotti che sono stati inseriti nel carrello avente l'id passato
    @Override
    public Carrello loadCarrello(Long idCart,String fotoPath) {
        PreparedStatement ps;
        ArrayList<ProdottoQty> contenutoCarrello=new ArrayList<ProdottoQty>();
        Carrello carrello=new Carrello();

        //Caricamento del carrello dal db
        try
        {
            String sql = "SELECT P.id_prod, nome_prod, descr, qty_disp, prezzo, foto_path, is_locked, C.id_cat,C.nome_cat, M.id_marca,M.nome_marca, CC.qty, P.deleted " +
                    "FROM (((Prodotto AS P INNER JOIN Marca AS M ON P.id_marca=M.id_marca) " +
                    "INNER JOIN Categoria AS C ON P.id_cat=C.id_cat) INNER JOIN Contenuto_carrello AS CC ON CC.id_prod=P.id_prod) " +
                    "WHERE CC.qty>0 AND CC.id_cart=? ORDER BY P.id_prod";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, idCart);
            ResultSet resultSet = ps.executeQuery();

            carrello.setIdCart(idCart);
            while (resultSet.next()) {
                ProdottoQty p= read(resultSet);
                p.getProd().setFotoProdotto(ProdottoDAOMySQLJDBCImpl.loadFotoProdotto(p.getProd(),fotoPath));
                contenutoCarrello.add(p);
            }
            resultSet.close();
            ps.close();
            carrello.setComposizione(contenutoCarrello);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carrello;
    }

    static ProdottoQty read(ResultSet rs) {

        ProdottoQty prodQty = new ProdottoQty();
        try {
            prodQty.setQty(rs.getInt("qty"));
        } catch (SQLException sqle) { }
        prodQty.setProd(ProdottoDAOMySQLJDBCImpl.read(rs));
        return prodQty;
    }
}
