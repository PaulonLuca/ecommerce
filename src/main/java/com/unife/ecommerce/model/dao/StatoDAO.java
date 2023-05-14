package com.unife.ecommerce.model.dao;

import com.unife.ecommerce.model.mo.Stato;

import java.util.ArrayList;

public interface StatoDAO {

    public Stato findStatoByDescr(String descr);

    public ArrayList<Stato> findAll();
}
