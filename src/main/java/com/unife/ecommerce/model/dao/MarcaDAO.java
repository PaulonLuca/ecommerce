package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.dao.exception.DuplicatedObjectException;
import com.unife.ecommerce.model.mo.Marca;
import java.util.ArrayList;

public interface MarcaDAO {

    //Ricerca di tutte le marche
    public ArrayList<Marca> findAllMarche();

    //Caricamento della marca avente l'id passato
    public Marca findMarcaById(Long idMarca);

    //Inserimento della nuova marca nel db
    public Marca create(String nome) throws DuplicatedObjectException;
}
