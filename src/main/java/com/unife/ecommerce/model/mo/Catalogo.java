package com.unife.ecommerce.model.mo;

public class Catalogo {
    private Long idCatal;
    private String nomeCatal;
    // (1,n) array di cetegorie contenute nel catalogo
    private Categoria[] categorie;


    public Long getIdCatal() { return idCatal;}

    public void setIdCatal(Long idCatal) { this.idCatal = idCatal; }

    public String getNomeCatal() { return nomeCatal; }

    public void setNomeCatal(String nomeCatal) { this.nomeCatal = nomeCatal; }

    public Categoria[] getCategorie() { return categorie;}

    public void setCategorie(Categoria[] categorie) { this.categorie = categorie;}
}
