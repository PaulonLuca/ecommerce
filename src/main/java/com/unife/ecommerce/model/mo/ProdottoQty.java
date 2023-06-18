package com.unife.ecommerce.model.mo;

//Reppresenta la coppia prodotto-quantità, serve per tradurre la relazione n,m tra ordine e prodotto. Gestisce
//la composizione dell'ordine
public class ProdottoQty {
    private Prodotto prod;
    private int qty;

    public Prodotto getProd() { return prod;}

    public void setProd(Prodotto prod) {    this.prod = prod;}

    public int getQty() {   return qty;}

    public void setQty(int qty) {   this.qty = qty;}
}
