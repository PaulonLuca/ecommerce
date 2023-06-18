package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

//Oggetto che mappa la tipologia di pagamento
public class TipoPagamento {
    private Long idTipoPag;
    private String nomeTipoPag;
    //(0,n) array di pagamenti di questo tipo
    private ArrayList<Pagamento> pagamenti;
    private boolean deleted;

    public Long getIdTipoPag() { return idTipoPag;}

    public void setIdTipoPag(Long idTipoPag) { this.idTipoPag = idTipoPag;}

    public String getNomeTipoPag() { return nomeTipoPag;}

    public void setNomeTipoPag(String nomeTipoPag) { this.nomeTipoPag = nomeTipoPag; }

    public ArrayList<Pagamento> getPagamenti() {    return pagamenti;   }

    public void setPagamenti(ArrayList<Pagamento> pagamenti) {  this.pagamenti = pagamenti; }

    public boolean isDeleted() {    return deleted; }

    public void setDeleted(boolean deleted) {   this.deleted = deleted; }
}
