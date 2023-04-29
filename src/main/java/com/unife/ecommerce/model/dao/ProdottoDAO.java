package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

//Interfaccia implementata in MySql
public interface ProdottoDAO {

    public Prodotto create(Long idProd, String nomeProd, String descr, int qtyDisp, double prezzo, String photoPath, boolean isLocked, Marca marca, Categoria cat, boolean deleted);

    public Prodotto update(Prodotto prod);

    public Prodotto delete(Prodotto prod);

    public Prodotto findProdottoById(Long id);

    public ArrayList<Prodotto> findAllProdotti(String searchString, HttpServletRequest request);

}
