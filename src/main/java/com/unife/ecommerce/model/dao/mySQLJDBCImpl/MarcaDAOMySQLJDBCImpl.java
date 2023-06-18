package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.MarcaDAO;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
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

    //Caricamento di tutte le merche dal db
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

    //Caricamento della marca avente l'id specificato dal db
    @Override
    public Marca findMarcaById(Long idMarca) {
        PreparedStatement ps;
        Marca marca = new Marca();
        try {
            String sql = " SELECT * FROM Marca WHERE id_marca=?";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++,idMarca);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                marca=read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return marca;
    }

    //Inserimento di una nuova marca nel db, si recupera il contatore della marca dalla tabella counter e si
    //verifica se vengono inseriti anche dei duplicati
    @Override
    public Marca create(String nome) throws DuplicatedObjectException {
        Marca marca = null;
        try {
            //verifica se la marca è già presente nel database
            String sql = " SELECT id_marca FROM Marca WHERE nome_marca=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, nome);
            ResultSet resultSet = ps.executeQuery();
            boolean exist = resultSet.next();
            resultSet.close();
            //se già presenta viene lanciata un'eccezione
            if (exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: Tentativo di inserimento di una marca già esistente.");
            }

            //recupera il valore a cui è arrivato l'id_marca dalla tabella di utilità dopo averlo incrementato di 1
            sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
            resultSet.next();

            marca = new Marca();
            marca.setIdMarca(resultSet.getLong("counter_value"));
            marca.setNomeMarca(nome);
            marca.setDeleted(false);

            resultSet.close();

            sql = "INSERT INTO Marca VALUES (?,?,?)";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, marca.getIdMarca());
            ps.setString(i++, marca.getNomeMarca());
            ps.setBoolean(i++, marca.isDeleted());
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return marca;
    }

    //Lettura dei campi relativi al record marca per crearene l'oggetto
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
