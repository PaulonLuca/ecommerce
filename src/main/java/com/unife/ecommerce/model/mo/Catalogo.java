package com.unife.ecommerce.model.mo;

import java.util.ArrayList;

public class Catalogo {
    private Long idCatal;
    private String nomeCatal;
    // (1,n) array di cetegorie contenute nel catalogo
    private ArrayList<Categoria> categorie;
    private boolean deleted;


    public Long getIdCatal() { return idCatal;}

    public void setIdCatal(Long idCatal) { this.idCatal = idCatal; }

    public String getNomeCatal() { return nomeCatal; }

    public void setNomeCatal(String nomeCatal) { this.nomeCatal = nomeCatal; }

    public ArrayList<Categoria> getCategorie() { return categorie; }

    public void setCategorie(ArrayList<Categoria> categorie) { this.categorie = categorie;}

    public boolean isDeleted() { return deleted;}

    public void setDeleted(boolean deleted) { this.deleted = deleted;}
}
