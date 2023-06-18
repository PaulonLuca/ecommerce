package com.unife.ecommerce.model.mo;

//Rappresenta l'oggetto carrallo qty, serve per tradurre la relazione tra carrello e prodotto
//con lato carrello con attributo qty sulla relazione.
public class CarrelloQty {
    private Carrello carrello;
    private int qty;

    public Carrello getCarrello() { return carrello;}

    public void setCarrello(Carrello carrello) { this.carrello = carrello;}

    public int getQty() { return qty;}

    public void setQty(int qty) { this.qty = qty;}
}
