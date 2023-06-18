package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;
import java.util.ArrayList;

//Interfaccia implementata in MySql
public interface ProdottoDAO {

    //Inserimento di un nuovo prodotto nel database
    public Prodotto create(Long idProd, String nomeProd, String descr, int qtyDisp, double prezzo, String photoPath, boolean isLocked, Marca marca, Categoria cat, boolean deleted, ArrayList<Long> fornitori);

    //Aggiornamento dei campi relativi ad un prodotto esistente
    public void update(Prodotto oldProd, Prodotto newProd);

    //Aggiunta e rimozione di prodotti in vetrina
    public void updateVetrina(Prodotto prod,boolean newInVetrina);

    //Eliminazione di un prodotto dal carrello
    public Prodotto delete(Prodotto prod);

    //Caricamento prodotto avente l'id specificato
    public Prodotto findProdottoById(Long idProd, String fotopath);

    //Caricamento di tutti i prodotti dal db
    public ArrayList<Prodotto> findAllProdotti( String fotoPath,String idCat,String idMarca, String searchString);

    //Caricmanto di tutti i prodotti dal db (bloccatti e qty=0=
    public ArrayList<Prodotto> findAllProdottiAdmin( String fotoPath,String idCat,String idMarca, String searchString);

    //Carimento di tutti i prodotti che appartengono alla vetrina
    public ArrayList<Prodotto> findProdottiVetrina(Long idVetrina, String fotoPath);

    //Aggiornamento quantità dispoibile del prodotto
    public boolean updateQty(Long idProd, int qty);

    //Verifica la quantità disponibile di un prodotto
    public boolean checkQtyDisp(Long idProd, int qty);

    //Verifica se un prodotto è in vetrina
    public boolean isInVetrina(Long idProd);

}
