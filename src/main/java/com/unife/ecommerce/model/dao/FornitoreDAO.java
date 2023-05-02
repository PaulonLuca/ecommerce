package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Fornitore;

import java.util.ArrayList;

public interface FornitoreDAO {

    public ArrayList<Fornitore> findAllFornitoriForProduct(Long idProd);
}
