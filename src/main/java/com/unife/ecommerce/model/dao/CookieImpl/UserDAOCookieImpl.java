package com.unife.ecommerce.model.dao.CookieImpl;

import com.unife.ecommerce.model.dao.UserDAO;
import com.unife.ecommerce.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    //riceve alla creazione request e response
    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    //creazione dell'utente loggato nei cookie
    @Override
    public Utente create(Long idUtente, String nome, String cognome, String email, String username, String psw, String tel, String citta, String via, String civico, String cap, boolean isAdmin, boolean isLocked, boolean deleted) {
        Utente loggedUser = new Utente();
        loggedUser.setIdUtente(idUtente);
        loggedUser.setNome(nome);
        loggedUser.setCognome(cognome);
        loggedUser.setEmail(email);
        loggedUser.setUsername(username);
        loggedUser.setAdmin(isAdmin);
        /*loggedUser.setTel(tel);
        loggedUser.setCitta(citta);
        loggedUser.setVia(via);
        loggedUser.setCivico(civico);
        loggedUser.setCap(cap);
        loggedUser.setLocked(isLocked);*/

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    //aggiorna parametri dell'utente loggato nei cookies
    @Override
    public void update(Utente loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //elimina il cookie con le informazioni dell'utente loggato dal client. Serve per effetture il logout
    @Override
    public void delete(Utente loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //itera su tutti i cookie alla ricerca quello corrispondende al logged user
    //se lo trova allora l'utente è loggato e ne recupera le informazioni personali
    @Override
    public Utente findLoggedUser() {
        Cookie[] cookies = request.getCookies();
        Utente loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;
    }

    @Override
    public Utente findByUserId(Long userId) {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public Utente findByUsername(String username) {throw new UnsupportedOperationException("Not supported yet.");}

    private String encode(Utente loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getIdUtente() + "#" + loggedUser.getNome() + "#" +
                loggedUser.getCognome()+ "#" + loggedUser.getEmail()+ "#" + loggedUser.getUsername()+ "#" + loggedUser.isAdmin();
                /*+ "#" + loggedUser.getCitta()+ "#" + loggedUser.getVia()+ "#"
                + loggedUser.getCivico()+ "#" + loggedUser.getCap()+ "#" + loggedUser.isAdmin()+ "#" + loggedUser.isLocked();*/
        return encodedLoggedUser;

    }

    private Utente decode(String encodedLoggedUser) {

        Utente loggedUser = new Utente();
        String[] values = encodedLoggedUser.split("#");

        loggedUser.setIdUtente(Long.parseLong(values[0]));
        loggedUser.setNome(values[1]);
        loggedUser.setCognome(values[2]);
        loggedUser.setEmail(values[3]);
        loggedUser.setUsername(values[4]);
        loggedUser.setAdmin(Boolean.valueOf(values[5]));
        /*loggedUser.setTel(values[5]);
        loggedUser.setCitta(values[6]);
        loggedUser.setVia(values[7]);
        loggedUser.setCivico(values[8]);
        loggedUser.setCap(values[9]);
        loggedUser.setAdmin(Boolean.valueOf(values[10]));
        loggedUser.setLocked(Boolean.valueOf(values[11]));*/

        return loggedUser;

    }
}
