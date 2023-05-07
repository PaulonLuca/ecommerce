package com.unife.ecommerce.model.dao.CookieImpl;

import com.unife.ecommerce.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}


    @Override
    public UserDAO getUserDAO() {   return new UserDAOCookieImpl(request,response); }

    @Override
    public CarrelloDAO getCarrelloDAO() {   return new CarrelloDAOCookieImpl(request,response); }

    @Override
    public ProdottoDAO getProdottoDAO() {   throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public MarcaDAO getMarcaDAO() { throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public CategoriaDAO getCategoriaDAO() {  throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public FornitoreDAO getFornitoreDAO() {  throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public SpedizioneDAO getSpedizioneDAO() {  throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public TipoPagamentoDAO getTipoPagamentoDAO() {  throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public IndirizzoSpedizioneDAO getIndirizzoSpedizioneDAO() {  throw new UnsupportedOperationException("Not supported yet."); }


}
