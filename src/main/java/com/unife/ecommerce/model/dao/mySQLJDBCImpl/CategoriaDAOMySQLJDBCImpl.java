package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.CategoriaDAO;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDAOMySQLJDBCImpl implements CategoriaDAO {

    private final String COUNTER_ID = "id_cat";
    Connection conn;

    public CategoriaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    //Caricamento di tutte le categorie disponibili dal db
    @Override
    public ArrayList<Categoria> findAllCategorie() {
        PreparedStatement ps;
        ArrayList<Categoria> categorie = new ArrayList<Categoria>();
        try {
            String sql = " SELECT * FROM Categoria ";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                categorie.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorie;
    }

    //Caricamento della categoria evente l'id specificato dal db. La categotia viene inserita anche nel catalogo
    @Override
    public Categoria findCategoriaById(Long idCat) {
        PreparedStatement ps;
        Categoria cat = new Categoria();
        try {
            String sql = " SELECT * FROM Categoria WHERE id_cat=?";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++,idCat);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                cat=read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cat;
    }

    //Inserimento della nuova categoria nel db
    @Override
    public Categoria create(String nome)  throws DuplicatedObjectException {
        Categoria categoria = null;
        try {
            //verifica se la categoria è già presente nel database
            String sql = " SELECT id_cat FROM Categoria WHERE nome_cat=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, nome);
            ResultSet resultSet = ps.executeQuery();
            boolean exist = resultSet.next();
            resultSet.close();
            //se già presenta viene lanciata un'eccezione
            if (exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: Tentativo di inserimento di una categoria già esistente.");
            }

            //recupera il valore a cui è arrivato l'id_cat dalla tabella di utilità dopo averlo incrementato di 1
            sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
            resultSet.next();

            categoria = new Categoria();
            categoria.setIdCat(resultSet.getLong("counter_value"));
            categoria.setNomeCat(nome);
            categoria.setDeleted(false);

            resultSet.close();

            sql = "INSERT INTO Categoria VALUES (?,?,?)";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, categoria.getIdCat());
            ps.setString(i++, categoria.getNomeCat());
            ps.setBoolean(i++, categoria.isDeleted());
            ps.executeUpdate();

            Long idCatalogo=1L;
            sql = "INSERT INTO Contiene_catalogo VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            i=1;
            ps.setLong(i++, idCatalogo);
            ps.setLong(i++, categoria.getIdCat());
            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return categoria;
    }

    //Lettura dei record relativi alla categotia per creare l'oggetto
    static Categoria read(ResultSet rs)
    {
        Categoria cat = new Categoria();
        try {
            cat.setIdCat(rs.getLong("id_cat"));
        } catch (SQLException sqle) { }
        try {
            cat.setNomeCat(rs.getString("nome_cat"));
        } catch (SQLException sqle) { }

        return cat;
    }


}
