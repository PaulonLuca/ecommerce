package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Categoria;

import java.util.ArrayList;

public interface CategoriaDAO {

    public ArrayList<Categoria> findAllCategorie();

    public Categoria findCategoriaById(Long idCat);
}
