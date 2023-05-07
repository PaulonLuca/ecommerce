<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
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
    <%@include file="/include/htmlHead.inc" %>
    <script>
        function deleteProdotto(code) {
            document.modifyCartForm.selectedProduct.value = code;
            document.modifyCartForm.controllerAction.value = "CartManagement.remove";
            document.modifyCartForm.submit();
        }
        function modifyProdotto(code) {
            document.modifyCartForm.selectedProduct.value = code;
            document.modifyCartForm.controllerAction.value = "CatalogManagement.view";
            document.modifyCartForm.submit();
        }
    </script>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Immagine</th>
                <th scope="col">Nome</th>
                <th scope="col">Quantit&agrave;</th>
                <th scope="col">Prezzo</th>
                <th scope="col"> Modifica</th>
                <th scope="col"> Elimina</th>
            </tr>
            </thead>
            <tbody>
            <% for(int i=0; i<carrello.getComposizione().size();i++) {%>
                <tr>
                    <th><img src="uploadedImages/<%=carrello.getComposizione().get(i).getProd().getIdProd()%>/<%=carrello.getComposizione().get(i).getProd().getFotoProdotto()[0].getName()%>" style="width: 50px; height: 50px;"></th>
                    <th><%=carrello.getComposizione().get(i).getProd().getNomeProd()%></th>
                    <th><%=carrello.getComposizione().get(i).getQty()%></th>
                    <th> &euro; <%=carrello.getComposizione().get(i).getQty()*carrello.getComposizione().get(i).getProd().getPrezzo()%></th>
                    <th><input type="button" class="btn btn-primary" id="btnModifica" nome="btnModifica" value="Modifica" onclick="modifyProdotto(<%=carrello.getComposizione().get(i).getProd().getIdProd()%>)"></th>
                    <th><input type="button" class="btn btn-primary" id="btnElimina" nome="btnElimina" value="Elimina" onclick="deleteProdotto(<%=carrello.getComposizione().get(i).getProd().getIdProd()%>)"></th>
                </tr>
            <%}%>
            </tbody>
        </table>
        <h2 style="margin-top: 40px;">Totale carrello:
            <%
                double totale=0;
                for(int i=0; i<carrello.getComposizione().size();i++) {
                    totale+=carrello.getComposizione().get(i).getProd().getPrezzo()*carrello.getComposizione().get(i).getQty();
            }%>
            &euro; <%=totale%>
        </h2>
    </section>
</main>

<form name="modifyCartForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value=""/>
    <input type="hidden" name="selectedProduct" value=""/>
</form>

<form name="searchForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
    <input type="hidden" name="selectedCat" value=""/>
    <input type="hidden" name="selectedMarca" value=""/>
    <input type="hidden" name="searchString" value=""/>
</form>

</body>

<%@include file="/include/footer.jsp" %>

</html>