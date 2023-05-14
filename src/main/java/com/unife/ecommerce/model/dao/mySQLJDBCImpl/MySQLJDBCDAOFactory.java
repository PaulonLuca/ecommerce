package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.unife.ecommerce.model.dao.*;
import com.unife.ecommerce.services.config.Configuration;

public class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //CREARE un metodo per ogni implemantazione di un dao per mysql

    @Override
    public UserDAO getUserDAO() {   return new UserDAOMySQLJDBCImpl(connection);    }

    @Override
    public ProdottoDAO getProdottoDAO() {return new ProdottoDAOMySQLJDBCImpl(connection);}

    @Override
    public MarcaDAO getMarcaDAO() {return new MarcaDAOMySQLJDBCImpl(connection); }

    @Override
    public CategoriaDAO getCategoriaDAO() { return new CategoriaDAOMySQLJDBCImpl(connection);    }

    @Override
    public FornitoreDAO getFornitoreDAO() { return new FornitoreDAOMySQLJDBCImpl(connection);    }

    @Override
    public CarrelloDAO getCarrelloDAO() { return new CarrelloDAOMySQLJDBCImpl(connection);    }

    @Override
    public SpedizioneDAO getSpedizioneDAO() { return new SpedizioneDAOMySQLJDBCImpl(connection);    }

    @Override
    public TipoPagamentoDAO getTipoPagamentoDAO() { return new TipoPagamentoDAOMySQLJDBCImpl(connection);    }

    @Override
    public IndirizzoSpedizioneDAO getIndirizzoSpedizioneDAO() { return new IndirizzoSpedizioneDAOMySQLJDBCImpl(connection);}

    @Override
    public PagamentoDAO getPagamentoDAO() {return new PagamentoDAOMySQLJDBCImpl(connection);}

    @Override
    public OrdineDAO getOrdineDAO() {return new OrdineDAOMySQLJDBCImpl(connection);}

    @Override
    public StatoDAO getStatoDAO() { return new StatoDAOMySQLJDBCImpl(connection);}


}
