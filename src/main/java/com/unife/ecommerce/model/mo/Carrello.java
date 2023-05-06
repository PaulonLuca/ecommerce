package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Carrello {
    private Long idCart;
    private Utente utente;
    private ArrayList<ProdottoQty> composizione;
    private boolean deleted;

    public Long getIdCart() {return idCart;}

    public void setIdCart(Long idCart) { this.idCart = idCart;}

    public Utente getUtente() { return utente;}

    public void setUtente(Utente utente) { this.utente = utente;}

    public ArrayList<ProdottoQty> getComposizione() {return composizione;}

    public void setComposizione(ArrayList<ProdottoQty> composizione) {this.composizione = composizione;}

    public boolean isDeleted() { return deleted;}

    public void setDeleted(boolean deleted) { this.deleted = deleted;}
}
