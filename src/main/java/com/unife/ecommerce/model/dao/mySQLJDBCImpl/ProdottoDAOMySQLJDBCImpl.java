package com.unife.ecommerce.model.dao.mySQLJDBCImpl;

import com.unife.ecommerce.model.dao.ProdottoDAO;
import com.unife.ecommerce.model.mo.Categoria;
import com.unife.ecommerce.model.mo.Marca;
import com.unife.ecommerce.model.mo.Prodotto;

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
    public Prodotto create(Long idProd, String nomeProd, String descr, int qtyDisp, double prezzo, String photoPath, boolean isLocked, Marca marca, Categoria cat, boolean deleted, ArrayList<Long> fornitori) {

        Prodotto prodotto = null;
        try {
            //recupera il valore a cui è arrivato l'id_prod dalla tabella di utilità dopo averlo incrementato di 1
            String sql = "update Counter set counter_value=counter_value+1 where id_counter='" + COUNTER_ID + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "SELECT counter_value FROM Counter where id_counter='" + COUNTER_ID + "'";
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            prodotto = new Prodotto();
            prodotto.setIdProd(resultSet.getLong("counter_value"));
            prodotto.setNomeProd(nomeProd);
            prodotto.setFotoPath(photoPath+"/"+prodotto.getIdProd()+"/");
            prodotto.setFotoProdotto(null);//da riempire
            prodotto.setDescr(descr);
            prodotto.setPrezzo(prezzo);
            prodotto.setQtyDisp(qtyDisp);
            prodotto.setLocked(isLocked);
            prodotto.setMarca(marca);
            prodotto.setCat(cat);
            prodotto.setDeleted(deleted);

            resultSet.close();

            sql = "INSERT INTO Prodotto VALUES (?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, prodotto.getIdProd());
            ps.setString(i++, prodotto.getNomeProd());
            ps.setString(i++, prodotto.getDescr());
            ps.setInt(i++, prodotto.getQtyDisp());
            ps.setDouble(i++, prodotto.getPrezzo());
            ps.setString(i++, prodotto.getFotoPath());
            ps.setBoolean(i++, prodotto.isLocked());
            ps.setLong(i++, prodotto.getCat().getIdCat());
            ps.setLong(i++, prodotto.getMarca().getIdMarca());
            ps.setBoolean(i++, prodotto.isDeleted());
            ps.executeUpdate();

            //Inserimento composizione fornitori prodotto
            for(int j=0;j<fornitori.size();j++)
            {
                sql="INSERT INTO Fornitura VALUES(?,?)";
                ps = conn.prepareStatement(sql);
                i=1;
                ps.setLong(i++,prodotto.getIdProd());
                ps.setLong(i++,fornitori.get(j));
                ps.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prodotto;
    }

    @Override
    public void update(Prodotto oldProd,Prodotto newProd) {
        PreparedStatement ps;
        try
        {
            ArrayList<String> sqlArray=new ArrayList<>();

            String sql = "UPDATE Prodotto SET ";

            if(!oldProd.getNomeProd().equals(newProd.getNomeProd()))
                sqlArray.add("nome_prod=? ");

            if(!oldProd.getDescr().equals(newProd.getDescr()))
                sqlArray.add("descr=? ");

            if(oldProd.getQtyDisp()!=newProd.getQtyDisp())
                sqlArray.add("qty_disp=? ");

            if(oldProd.getPrezzo()!=newProd.getPrezzo())
                sqlArray.add("prezzo=? ");

            if(oldProd.isLocked()!=newProd.isLocked())
                sqlArray.add("is_locked=? ");

            if(sqlArray.size()==1)
                sql+=sqlArray.get(0);
            else
            {
                for(int j=0;j< sqlArray.size();j++)
                    if(j==sqlArray.size()-1)
                        sql+=sqlArray.get(j);
                    else
                        sql+=sqlArray.get(j)+", ";
            }


            sql+="WHERE id_prod=?";

            ps = conn.prepareStatement(sql);
            int i=1;
            if(!oldProd.getNomeProd().equals(newProd.getNomeProd()))
                ps.setString(i++,newProd.getNomeProd());
            if(!oldProd.getDescr().equals(newProd.getDescr()))
                ps.setString(i++,newProd.getDescr());
            if(oldProd.getQtyDisp()!=newProd.getQtyDisp())
                ps.setInt(i++,newProd.getQtyDisp());
            if(oldProd.getPrezzo()!=newProd.getPrezzo())
                ps.setDouble(i++,newProd.getPrezzo());
            if(oldProd.isLocked()!=newProd.isLocked())
                ps.setBoolean(i++,newProd.isLocked());
            ps.setLong(i++,newProd.getIdProd());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateVetrina(Prodotto prod,boolean newInVetrina) {
        PreparedStatement ps;
        Long idVetrina=1L;
        try
        {
            //Se prodotto era in vetrina viene rimosso
            //Se non era in vetrina viene aggiunto
            String sql="";
            if(newInVetrina)
                sql="INSERT INTO In_evidenza VALUES (?,?)";
            else
                sql="DELETE FROM In_evidenza WHERE id_vetrina=? AND id_prod=? ";
            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++,idVetrina);
            ps.setLong(i++,prod.getIdProd());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                    "INNER JOIN Categoria AS C ON P.id_cat=C.id_cat) WHERE P.qty_disp>0";

            //Se presente la stringa di ricerca
            if (searchString != null && searchString != "") {
                sql += " AND (INSTR(nome_prod,?)>0 ";
                sql += " OR INSTR(descr,?)>0 ";
                sql += " OR INSTR(nome_marca,?)>0 ";
                sql += " OR INSTR(nome_cat,?))>0 ";
            }
            //Se si ricerca una determinata categoria
            if (idCat != null && idCat != "") {
                sql += " AND P.id_cat="+idCat;
            }
            //Se si ricerca una determinata marca
            if (idMarca != null && idMarca!="") {
                sql += " AND P.id_marca="+idMarca;
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
                    "WHERE V.id_vetrina=? AND P.qty_disp>0;";

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

    //Se il prodotto ha una disponobilità >= alla richiesta questa viene decrementata
    //altimenti non si decrementa
    @Override
    public boolean updateQty( Long idProd,int qty) {
        PreparedStatement ps;
        try
        {
            String sql = "UPDATE Prodotto SET qty_disp=qty_disp-? WHERE id_prod=? AND qty_disp-?>=0";

            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setInt(i++, qty);
            ps.setLong(i++, idProd);
            ps.setInt(i++, qty);
            int risultato = ps.executeUpdate();
            ps.close();

            //Se la quantità viene modificata
            if(risultato==1)
                return true;
            else
                return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkQtyDisp(Long idProd, int qty) {
        PreparedStatement ps;
        try
        {
            String sql = "SELECT * FROM Prodotto WHERE id_prod=? AND qty_disp-?>=0";

            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++, idProd);
            ps.setInt(i++, qty);
            ResultSet resultSet = ps.executeQuery();

            //Se il prodotto ha una quantità decrementabile sarà in soluzione
            if (resultSet.next())
            {
                resultSet.close();
                ps.close();
                return true;
            }
            else
            {
                resultSet.close();
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isInVetrina(Long idProd) {
        PreparedStatement ps;
        try
        {
            String sql = "SELECT * FROM In_evidenza WHERE id_prod=?";

            ps = conn.prepareStatement(sql);
            int i=1;
            ps.setLong(i++, idProd);
            ResultSet resultSet = ps.executeQuery();

            //Se il risultato contiene un record, allora il prdodotto è in vetrina
            if (resultSet.next())
            {
                resultSet.close();
                ps.close();
                return true;
            }
            else
            {
                resultSet.close();
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    static File[] loadFotoProdotto(Long idProd, String fotoPath){
        fotoPath+="/"+idProd.toString()+"/";
        File imageDir=new File(fotoPath);
        File[] fileList=imageDir.listFiles();

        if(fileList==null)
        {
            String[] s=fotoPath.split(File.separator);
            String path="";
            for(int i=0;i<s.length-2;i++)
                path+=s[i]+"/";
            path+="images/";
            imageDir=new File(path);
            fileList=imageDir.listFiles();
        }
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
