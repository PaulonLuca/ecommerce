package com.unife.ecommerce.model.mo;

public class TipoPagamento {
    private Long idTipoPag;
    private String nomeTipoPag;
    //(0,n) array di pagamenti di questo tipo
    private Pagamento[] pagamenti;

    public Long getIdTipoPag() { return idTipoPag;}

    public void setIdTipoPag(Long idTipoPag) { this.idTipoPag = idTipoPag;}

    public String getNomeTipoPag() { return nomeTipoPag;}

    public void setNomeTipoPag(String nomeTipoPag) { this.nomeTipoPag = nomeTipoPag; }

    public Pagamento[] getPagamenti() {return pagamenti;}

    public void setPagamenti(Pagamento[] pagamenti) {this.pagamenti = pagamenti;}
}
