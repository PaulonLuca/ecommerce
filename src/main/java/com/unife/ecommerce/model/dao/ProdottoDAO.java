package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;

import java.util.ArrayList;

//Interfaccia implementata in MySql
public interface ProdottoDAO {

    public Prodotto create(Long idProd, String nomeProd, String descr, int qtyDisp, double prezzo, String photoPath, boolean isLocked, Marca marca, Categoria cat, boolean deleted, ArrayList<Long> fornitori);

    public void update(Prodotto oldProd, Prodotto newProd);

    public void updateVetrina(Prodotto prod,boolean newInVetrina);

    public Prodotto delete(Prodotto prod);

    public Prodotto findProdottoById(Long idProd, String fotopath);

    public ArrayList<Prodotto> findAllProdotti( String fotoPath,String idCat,String idMarca, String searchString);

    public ArrayList<Prodotto> findProdottiVetrina(Long idVetrina, String fotoPath);

    public boolean updateQty(Long idProd, int qty);

    public boolean checkQtyDisp(Long idProd, int qty);

    public boolean isInVetrina(Long idProd);

}
