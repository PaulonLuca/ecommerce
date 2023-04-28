package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.UserDAO;
import com.unife.ecommerce.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    private final String COUNTER_ID = "id_utente";
    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Utente create(Long idUtente, String nome, String cognome, String email, String username, String psw, String tel, String citta, String via, String civico, String cap, boolean isAdmin, boolean isLocked, boolean deleted) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Utente user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Utente user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Utente findLoggedUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Utente findByUserId(Long idUtente) {
        PreparedStatement ps;
        Utente utente = null;
        try {
            String sql = " SELECT * " + "   FROM Utente " + " WHERE " + "   id_utente = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, idUtente);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    @Override
    public Utente findByUsername(String username) {
        PreparedStatement ps;
        Utente user = null;
        try {
            String sql = " SELECT * " + "   FROM Utente " + " WHERE " + "   username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    Utente read(ResultSet rs) {

        Utente user = new Utente();
        try {
            user.setIdUtente(rs.getLong("id_utente"));
        } catch (SQLException sqle) {
        }
        try {
            user.setNome(rs.getString("nome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCognome(rs.getString("cognome"));
        } catch (SQLException sqle) {
        }
        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException sqle) {
        }
        try {
            user.setUsername(rs.getString("username"));
        } catch (SQLException sqle) {
        }
        try {
            user.setPsw(rs.getString("psw"));
        } catch (SQLException sqle) {
        }
        try {
            user.setTel(rs.getString("tel"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCitta(rs.getString("citta"));
        } catch (SQLException sqle) {
        }
        try {
            user.setVia(rs.getString("via"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCivico(rs.getString("civico"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCap(rs.getString("cap"));
        } catch (SQLException sqle) {
        }
        try {
            user.setAdmin(rs.getBoolean("is_admin"));
        } catch (SQLException sqle) {
        }
        try {
            user.setLocked(rs.getBoolean("is_locked"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) {
        }
        return user;
    }
}
