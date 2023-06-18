package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.UserDAO;
import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    private final String COUNTER_ID = "id_utente";
    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    //Inseriemento di un nuovo utente nel db. Si utilizza la taballa counter per recuperare il punto
    //a cui è arrivato l'id utente
    @Override
    public Utente create(Long idUtente, String nome, String cognome, String email, String username, String psw, String tel, String citta, String via, String civico, String cap, boolean isAdmin, boolean isLocked, boolean deleted) throws DuplicatedObjectException{
        PreparedStatement ps;
        Utente user=null;
        try {
            //verifica se l'utente è già presente nel database
            String sql = " SELECT id_utente " + " FROM Utente " + " WHERE " + " deleted =false AND "  + " email = ? AND" + " username = ? ";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, email);
            ps.setString(i++, username);
            ResultSet resultSet = ps.executeQuery();
            boolean exist = resultSet.next();
            resultSet.close();
            //se già presenta viene lanciata un'eccezione
            if (exist) {
                throw new DuplicatedObjectException("ContactDAOJDBCImpl.create: Tentativo di inserimento di un utente già esistente.");
            }

            //recupera il valore a cui è arrivato l'id_utente dalla tabella di utilità dopo averlo incrementato di 1
            sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
            resultSet.next();
            //creazione del nuovo utente
            user=new Utente();
            user.setIdUtente(resultSet.getLong("counter_value"));
            user.setNome(nome);
            user.setCognome(cognome);
            user.setEmail(email);
            user.setUsername(username);
            user.setPsw(psw);
            user.setTel(tel);
            user.setAdmin(isAdmin);
            user.setLocked(isLocked);
            user.setCitta(citta);
            user.setVia(via);
            user.setCivico(civico);
            user.setCap(cap);
            user.setDeleted(deleted);

            resultSet.close();

            sql = "INSERT INTO Utente VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, user.getIdUtente());
            ps.setString(i++, user.getNome());
            ps.setString(i++, user.getCognome());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getUsername());
            ps.setString(i++, user.getPsw());
            ps.setString(i++, user.getTel());
            ps.setBoolean(i++, user.isAdmin());
            ps.setBoolean(i++, user.isLocked());
            ps.setString(i++, user.getCitta());
            ps.setString(i++, user.getVia());
            ps.setString(i++, user.getCivico());
            ps.setString(i++, user.getCap());
            ps.setBoolean(i++, user.isDeleted());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
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

    //Caricamento dell'utente dal db avente l'id specificato
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

    //Caricamento dell'utente dal db avente lo username specificato
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

    //Carica tutti gli utenti registrati non amministratori oppure gli utenti amministratori
    @Override
    public ArrayList<Utente> findAll(boolean isAdmin, Long idLogged) {
        PreparedStatement ps;
        ArrayList<Utente> utenti=new ArrayList<>();
        try {
            String sql = "SELECT * FROM Utente WHERE is_admin=? AND id_utente!=?";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setBoolean(i++,isAdmin);
            ps.setLong(i++,idLogged);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                utenti.add(read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;
    }

    //In modalitò amministratore è possibile specificare quele campo tra is_admine e is_locked
    //è possibile modificare specificandone il nuovo valore
    @Override
    public void updateField(Long idUtente, String field, boolean value) {
        PreparedStatement ps;
        try {
            String sql="UPDATE Utente SET " + field + "=? WHERE id_utente=?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setBoolean(i++, value);
            ps.setLong(i++, idUtente);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Lettura dei campi relativi al record utente per creare l'oggetto utente
    static Utente read(ResultSet rs) {

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
