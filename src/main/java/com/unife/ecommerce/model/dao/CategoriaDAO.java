package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.Categoria;
import java.util.ArrayList;

public interface CategoriaDAO {

    //Caricamnento di tutte le categorie
    public ArrayList<Categoria> findAllCategorie();

    //Caricamento di una categoria di cui si ha l'id
    public Categoria findCategoriaById(Long idCat);

    //Inserimento di una nuova categoria
    public Categoria create(String nome) throws DuplicatedObjectException;
}
