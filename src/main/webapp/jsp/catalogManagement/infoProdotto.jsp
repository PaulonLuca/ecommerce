<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%@ page session="false" %>
<%@ page errorPage="../errorPage.jsp"%>
<%
    //jsp per gestire l'inserimento o la modifica del prodotto
    //Se in modalità modifica: vengono precompilati i campi del prodotto selezionato dalla home page
    //e confermando la modifica questi vegono modificati anche nel db
    //Se in modalità inserimento: viene presentata una form vuota in cui è possibile compilare i campi
    //e creare il nuovo prodotto nel db.
    //Per ogni campo vengono fatti dei controlli lato client prima di sottomettere la form al server.

    boolean insertMode=(Boolean) request.getAttribute("insertMode");
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    boolean inVetrina=(Boolean) request.getAttribute("inVetrina");
    ArrayList<Fornitore> fornitori = (ArrayList<Fornitore>) request.getAttribute("fornitori");

    boolean isAdmin =(Boolean) request.getAttribute("isAdmin");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Marca> marche = (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie = (ArrayList<Categoria>) request.getAttribute("categorie");
    Carrello carrello=(Carrello) request.getAttribute("carrello");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp" %>
    <script>
        function validateForm() {

            if( document.productForm.qty_disp.value=="" ) {
                alert( "Il campo quantit\u00E0 non pu\u00F2 essere vuoto" );
                document.productForm.qty_disp.focus() ;
                return false;
            }

            if( isNaN(document.productForm.prezzo.value) ) {
                alert( "Il campo prezzo deve contenere solo valori numerici" );
                document.productForm.prezzo.focus() ;
                return false;
            }

            return( true );
        }
    </script>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <%if(insertMode){%>
        <h1>Inserire le caratteristiche del prodotto</h1>
    <%} else {%>
        <h2>Modificare i campi del prodotto</h2>
    <%}%>

    <form name="productForm" method="post" action="Dispatcher" class="form-control-lg formReg" onsubmit="return validateForm()">
        <fieldset>
            <table >
                <tr>
                    <td><label for="nome">Nome</label></td>
                    <td> <input type="text" id="nome"  name="nome" maxlength="100" value="<%=!insertMode? prodotto.getNomeProd(): ""%>" required></td>
                </tr>
                <tr>
                    <td><label for="descrizione">Descrizione</label></td>
                    <td><textarea  id="descrizione"  name="descrizione" maxlength="50"  required rows="8" cols="40"> <%=!insertMode? prodotto.getDescr(): ""%></textarea></td>
                </tr>
                <tr>
                    <td><label for="qty_disp">Quantit&agrave; disponibile</label></td>
                    <td><input type="number" id="qty_disp"  name="qty_disp" min="0" max="200" value="<%=!insertMode? prodotto.getQtyDisp(): ""%>" required></td>
                </tr>
                <tr>
                    <td><label for="prezzo">Prezzo &euro;</label></td>
                    <td><input type="text" id="prezzo"  name="prezzo" maxlength="40" value="<%=!insertMode? prodotto.getPrezzo(): ""%>" required></td>
                </tr>

                <tr>
                    <td><label for="categoria">Categoria</label></td>
                    <td>
                        <select id="categoria" name="categoria" >
                            <%for (int i=0;i<categorie.size();i++){%>
                            <option value="<%=categorie.get(i).getIdCat()%>" <%=!insertMode && categorie.get(i).getIdCat()==prodotto.getCat().getIdCat()? "selected":""%>>
                                <%=categorie.get(i).getNomeCat()%>
                            </option>
                            <%}%>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td><label for="marca">Marca</label></td>
                    <td>
                        <select id="marca" name="marca">
                            <%for (int i=0;i<marche.size();i++){%>
                            <option value="<%=marche.get(i).getIdMarca()%>" <%=!insertMode && marche.get(i).getIdMarca()==prodotto.getMarca().getIdMarca()? "selected":""%>>
                                <%=marche.get(i).getNomeMarca()%>
                            </option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <%if(insertMode){%>
                <tr>
                    <td><label for="fornitori">Fornitori</label></td>
                    <td>
                        <select id="fornitori" name="fornitori" multiple="multiple">
                            <%for (int i=0;i<fornitori.size();i++){%>
                            <option value="<%=fornitori.get(i).getIdFornitore()%>" <%=i==0? "selected":""%>
                                    <%-- if(!insertMode) {%>
                                        <% for(int j=0;j< prodotto.getFornitori().size();j++) {
                                            if(fornitori.get(i).getIdFornitore()==prodotto.getFornitori().get(j).getIdFornitore()) { %>
                                                "selected"
                                            <%}%>
                                        <%} }--%> >
                                <%=fornitori.get(i).getNomeFornitore()%>
                            </option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td><label for="vetrina">Vetrina</label></td>
                    <td><input type="checkbox" id="vetrina"  name="vetrina" <%=!insertMode && inVetrina? "checked":""%>  ></td>
                </tr>
                <tr>
                    <td><label for="bloccato">Bloccato</label></td>
                    <td><input type="checkbox" id="bloccato"  name="bloccato" <%=!insertMode && prodotto.isLocked()? "checked":""%> ></td>
                </tr>

            </table>

            <%if(!insertMode){%>
                <input type="submit" class="button" value="Modifica" style="margin-top: 15px;"/>
            <%} else {%>
                <input type="submit" class="button" value="Inserisci" style="margin-top: 15px;"/>
            <%}%>

            <input type="hidden" name="controllerAction" value="CatalogManagement.add"/>
            <input type="hidden" name="insertMode" value="<%=insertMode %>"/>
            <input type="hidden" name="selectedProd" value="<%=!insertMode? prodotto.getIdProd():null %>"/>
        </fieldset>
    </form>

</main>


<form name="searchForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
    <input type="hidden" name="selectedCat" value=""/>
    <input type="hidden" name="selectedMarca" value=""/>
    <input type="hidden" name="searchString" value=""/>
</form>

</body>

<%@include file="/include/footer.jsp" %>

</html>