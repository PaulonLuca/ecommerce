package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

//Oggetto che mappa l'utente
public class Utente {
    private Long idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String psw;
    private String tel;
    private boolean isAdmin;
    private boolean isLocked;
    private String citta;
    private String via;
    private String civico;
    private String cap;
    //(0,n) lista degli indirizzi di spedizione dell'utente
    private ArrayList<IndirizzoSpedizione> indirizziSped;
    //(0,n) lista degli ordini fatti dall'utente
    private ArrayList<Ordine> ordini;
    //(0,n) ogni utente può avere più carrelli
    private ArrayList<Carrello> carrelli;
    private boolean deleted;

    public Long getIdUtente() { return idUtente;}

    public void setIdUtente(Long idUtente) {this.idUtente = idUtente;}

    public String getNome() { return nome;}

    public void setNome(String nome) { this.nome = nome;}

    public String getCognome() {return cognome;}

    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPsw() { return psw;}

    public void setPsw(String psw) {this.psw = psw;}

    public String getTel() {return tel;}

    public void setTel(String tel) {this.tel = tel;}

    public boolean isAdmin() {return isAdmin;}

    public void setAdmin(boolean admin) {isAdmin = admin;}

    public boolean isLocked() {return isLocked;}

    public void setLocked(boolean locked) {isLocked = locked;}

    public String getCitta() {return citta;}

    public void setCitta(String citta) {this.citta = citta;}

    public String getVia() {return via;}

    public void setVia(String via) {this.via = via;}

    public String getCivico() {return civico;}

    public void setCivico(String civico) {this.civico = civico;}

    public String getCap() {return cap;}

    public void setCap(String cap) {this.cap = cap;}


    public ArrayList<IndirizzoSpedizione> getIndirizziSped() {  return indirizziSped;}

    public void setIndirizziSped(ArrayList<IndirizzoSpedizione> indirizziSped) {    this.indirizziSped = indirizziSped;}

    public ArrayList<Ordine> getOrdini() {  return ordini;}

    public void setOrdini(ArrayList<Ordine> ordini) {   this.ordini = ordini;}

    public boolean isDeleted() {    return deleted;}

    public void setDeleted(boolean deleted) {   this.deleted = deleted;}

    public ArrayList<Carrello> getCarrelli() { return carrelli;}

    public void setCarrelli(ArrayList<Carrello> carrelli) {this.carrelli = carrelli;}
}
