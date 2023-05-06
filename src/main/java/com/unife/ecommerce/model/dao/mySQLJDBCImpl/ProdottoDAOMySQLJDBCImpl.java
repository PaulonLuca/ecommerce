package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.ProdottoDAO;
import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;
import com.unife.ecommerce.model.mo.Utente;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdottoDAOMySQLJDBCImpl implements ProdottoDAO {

    private final String COUNTER_ID = "id_prod";
    Connection conn;

    public ProdottoDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Prodotto create(Long idProd, String nomeProd, String descr, int qtyDisp, double prezzo, String photoPath, boolean isLocked, Marca marca, Categoria cat, boolean deleted) {
        return null;
    }

    @Override
    public Prodotto update(Prodotto prod) {
        return null;
    }

    @Override
    public Prodotto delete(Prodotto prod) {
        return null;
    }

    @Override
    public Prodotto findProdottoById(Long idProd,String fotoPath) {
        PreparedStatement ps;
        Prodotto prod=new Prodotto();

        try
        {
            String sql = "SELECT id_prod, nome_prod, descr, qty_disp, prezzo, foto_path, is_locked, C.id_cat,C.nome_cat, M.id_marca,M.nome_marca, P.deleted " +
                    "FROM ((Prodotto AS P INNER JOIN Marca AS M ON P.id_marca=M.id_marca) " +
                    "INNER JOIN Categoria AS C ON P.id_cat=C.id_cat) WHERE P.id_prod=? ORDER BY id_prod ;";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idProd.toString());
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                prod= read(resultSet);
                prod.setFotoProdotto(loadFotoProdotto(prod.getIdProd(),fotoPath));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prod;
    }

    @Override
    public ArrayList<Prodotto> findAllProdotti( String fotoPath,String idCat,String idMarca,String searchString) {
        PreparedStatement ps;
        ArrayList<Prodotto> prodotti=new ArrayList<Prodotto>();

        //Lista dei fornitori del prodotto viene popolata nel controller usando il DAO Fornitore
        try
        {
            String sql = "SELECT id_prod, nome_prod, descr, qty_disp, prezzo, foto_path, is_locked, C.id_cat,C.nome_cat, M.id_marca,M.nome_marca, P.deleted " +
                    " FROM ((Prodotto AS P INNER JOIN Marca AS M ON P.id_marca=M.id_marca) " +
                    "INNER JOIN Categoria AS C ON P.id_cat=C.id_cat) ";

            //Se presente la stringa di ricerca
            if (searchString != null && searchString != "") {
                sql += " WHERE INSTR(nome_prod,?)>0 ";
                sql += " OR INSTR(descr,?)>0 ";
                sql += " OR INSTR(nome_marca,?)>0 ";
                sql += " OR INSTR(nome_cat,?)>0 ";
            }
            //Se si ricerca una determinata categoria
            if (idCat != null && idCat != "") {
                sql += " WHERE P.id_cat="+idCat;
            }
            //Se si ricerca una determinata marca
            if (idMarca != null && idMarca!="") {
                sql += " WHERE P.id_marca="+idMarca;
            }
            sql+=" ORDER BY id_prod ;";
            ps = conn.prepareStatement(sql);

            int i = 1;
            if (searchString != null && searchString != "") {
                ps.setString(i++, searchString);
                ps.setString(i++, searchString);
                ps.setString(i++, searchString);
                ps.setString(i++, searchString);
            }
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Prodotto p= read(resultSet);
                p.setFotoProdotto(loadFotoProdotto(p.getIdProd(),fotoPath));
                prodotti.add(p);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prodotti;
    }

    @Override
    public ArrayList<Prodotto> findProdottiVetrina(Long idVetrina,String fotoPath) {
        PreparedStatement ps;
        ArrayList<Prodotto> prodottiVetrina=new ArrayList<>();

        try
        {
            String sql = "SELECT P.id_prod" +
                    " FROM ((Prodotto AS P INNER JOIN In_evidenza AS IE ON P.id_prod = IE.id_prod) " +
                    "INNER JOIN Vetrina AS V ON IE.id_vetrina = V.id_vetrina ) " +
                    "WHERE V.id_vetrina=? ;";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idVetrina.toString());
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long idProd=Long.parseLong(resultSet.getString("id_prod"));
                prodottiVetrina.add(findProdottoById(idProd,fotoPath));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prodottiVetrina;
    }


    static File[] loadFotoProdotto(Long idProd, String fotoPath){
        fotoPath+="/"+idProd.toString()+"/";
        File imageDir=new File(fotoPath);
        File[] fileList=imageDir.listFiles();
        return fileList;
    }

    static Prodotto read(ResultSet rs) {

        Prodotto prod = new Prodotto();
        try {
            prod.setIdProd(rs.getLong("id_prod"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setNomeProd(rs.getString("nome_prod"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setDescr(rs.getString("descr"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setQtyDisp(rs.getInt("qty_disp"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setPrezzo(rs.getDouble("prezzo"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setFotoPath(rs.getString("foto_path"));
        } catch (SQLException sqle) {
        }
        try {
            prod.setDeleted(rs.getBoolean("deleted"));
        } catch (SQLException sqle) {
        }

        prod.setMarca(MarcaDAOMySQLJDBCImpl.read(rs));
        prod.setCat(CategoriaDAOMySQLJDBCImpl.read(rs));

        return prod;
    }

}
