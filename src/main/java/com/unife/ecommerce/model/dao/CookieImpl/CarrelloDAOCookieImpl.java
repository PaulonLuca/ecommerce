package com.unife.ecommerce.model.dao.CookieImpl;

import com.unife.ecommerce.model.dao.CarrelloDAO;
import com.unife.ecommerce.model.mo.Carrello;
import com.unife.ecommerce.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CarrelloDAOCookieImpl implements CarrelloDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    //riceve alla creazione request e response
    public CarrelloDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    //Creazione carrello nei cookies
    @Override
    public Carrello create(Long idCart, Utente utente) {
        Carrello carrello = new Carrello();
        carrello.setIdCart(idCart);

        Cookie cookie;
        cookie = new Cookie("carrello", encode(carrello));
        cookie.setPath("/");
        response.addCookie(cookie);

        return carrello;
    }

    //eliminazione cookie carrello
    @Override
    public void destroy() {
        Cookie cookie;
        cookie = new Cookie("carrello", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //recupero id carrello dai cookies
    @Override
    public Carrello getCookieCart() {
        Cookie[] cookies = request.getCookies();
        Carrello carrello = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && carrello == null; i++) {
                if (cookies[i].getName().equals("carrello")) {
                    carrello = decode(cookies[i].getValue());
                }
            }
        }

        return carrello;
    }

    @Override
    public void add(Long idCart, Long idProd, int qty) {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public void delete(Long idCart, Long idProd) {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public Carrello loadCarrello(Long idCart,String fotoPath) {throw new UnsupportedOperationException("Not supported yet.");}

    //Crea una stringa con le principali informazioni del carrello
    private String encode(Carrello carrello) {
        String encodedCarrello;
        encodedCarrello = carrello.getIdCart() + "#";
        return encodedCarrello;
    }

    //Dalla stringa con le informazioni dell'utente si crea l'oggetto utente
    private Carrello decode(String encodedCarrello) {
        Carrello carrello = new Carrello();
        String[] values = encodedCarrello.split("#");
        carrello.setIdCart(Long.parseLong(values[0]));
        return carrello;

    }
}
