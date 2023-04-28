package com.unife.ecommerce.model.mo;

//rappresanta la coppia ordine qty, serve per tradurre la relzione n,m tra ordine e prodotto
public class OrdineQty {
    private Ordine ord;
    private int qty;

    public Ordine getOrd() {    return ord; }

    public void setOrd(Ordine ord) {    this.ord = ord;}

    public int getQty() {   return qty;}

    public void setQty(int qty) {   this.qty = qty; }
}
