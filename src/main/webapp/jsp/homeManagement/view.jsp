<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.Prodotto" %>
<%@ page import="com.unife.ecommerce.model.mo.Marca" %>
<%@ page import="com.unife.ecommerce.model.mo.Categoria" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Prodotto> prodotti= (ArrayList<Prodotto>) request.getAttribute("prodotti");
    ArrayList<Marca> marche= (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie= (ArrayList<Categoria>) request.getAttribute("categorie");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <script>
        function viewDettagliProdotto(code) {
            document.selectionForm.selectedProduct.value = code;
            document.selectionForm.submit();
        }
    </script>
</head>
<body>
    <%@include file="/include/header.jsp"%>

<main class="clearfix" style="float:left; margin: 0 auto;">
    <% for( int i=0; i<prodotti.size(); i++) { %>

    <% if(i%3==0) {%> <div style="clear: both;"></div> <%}%>
    <article style="float:left; margin: auto;">
        <div class="card" style="width: 18rem; ">
            <img src="uploadedImages/<%=prodotti.get(i).getIdProd()%>/<%=prodotti.get(i).getFotoProdotto()[0].getName()%>" class="card-img-top" alt="...">
            <div class="card-body">
                <h5 class="card-title"><%= prodotti.get(i).getNomeProd()%></h5>
                <p class="card-text">&euro; <%= prodotti.get(i).getPrezzo()%></p>
                <a href="javascript:viewDettagliProdotto(<%=prodotti.get(i).getIdProd()%>)" class="btn btn-primary">Go somewhere</a>
            </div>
        </div>
    </article>
   <% }%>

</main>
    <aside>
        <h2>Vetrina</h2>
    </aside>

    <div style="clear: both;"></div>

    <nav aria-label="Navigation" style="margin: 0 auto;">
        <ul class="pagination">
            <li class="page-item"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </nav>

    <form name="selectionForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.viewDetail"/>
        <input type="hidden" name="selectedProduct" value=""/>
    </form>

    <form name="searchForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
        <input type="hidden" name="selectedCat" value=""/>
        <input type="hidden" name="selectedMarca" value=""/>
        <input type="hidden" name="searchString" value=""/>
    </form>

</body>

<%@include file="/include/footer.inc"%>

</html>