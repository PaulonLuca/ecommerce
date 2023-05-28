package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Marca;

import java.util.ArrayList;

public interface MarcaDAO {

    public ArrayList<Marca> findAllMarche();

    public Marca findMarcaById(Long idMarca);
}
