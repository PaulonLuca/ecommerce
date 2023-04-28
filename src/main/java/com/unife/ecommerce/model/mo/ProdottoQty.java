package com.unife.ecommerce.model.mo;

//reppresenta la coppia prodotto-quantità, serve per tradurre la relzione n,m tra ordine e prodotto
public class ProdottoQty {
    private Prodotto prod;
    private int qty;

    public Prodotto getProd() { return prod;}

    public void setProd(Prodotto prod) {    this.prod = prod;}

    public int getQty() {   return qty;}

    public void setQty(int qty) {   this.qty = qty;}
}
