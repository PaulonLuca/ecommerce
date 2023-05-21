<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
    boolean isAdmin =(Boolean) request.getAttribute("isAdmin");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    ArrayList<Marca> marche = (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie = (ArrayList<Categoria>) request.getAttribute("categorie");
    ArrayList<Prodotto> prodottiVetrina = (ArrayList<Prodotto>) request.getAttribute("prodottiVetrina");
    Carrello carrello=(Carrello) request.getAttribute("carrello");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    //int numPagine=(Integer) request.getAttribute("numPagine") ;
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp" %>
    <script>
        function viewDettagliProdotto(code) {
            document.selectionForm.selectedProduct.value = code;
            document.selectionForm.submit();
        }
        function viewPage(pageIndex) {
            document.paginationForm.paginationIndex.value = pageIndex;
            document.paginationForm.submit();
        }
    </script>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section class="vetrina">
        <div id="carouselVetrina" class="carousel slide carousel-dark" data-bs-ride="carousel">
            <div class="carousel-indicators">
                <button type="button" data-bs-target="#carouselVetrina" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1" ></button>
                <% for (int i = 1; i < prodottiVetrina.size(); i++) { %>
                    <button type="button" data-bs-target="#carouselVetrina" data-bs-slide-to="<%= i%>" aria-label="Slide <%= i+1%>" ></button>
                <%}%>
            </div>
            <div class="carousel-inner">
                <% for (int i = 0; i < prodottiVetrina.size(); i++) { %>
                <div class="carousel-item active" data-bs-interval="1700">
                    <img src="uploadedImages/<%=prodottiVetrina.get(i).getIdProd()%>/<%=prodottiVetrina.get(i).getFotoProdotto()[0].getName()%>" class="d-block imgVetrina" >
                    <div class="carousel-caption d-none d-md-block">
                        <h2 style="color: red;">&euro; <%=  prodottiVetrina.get(i).getPrezzo()%></h2>
                        <p><a href="javascript:viewDettagliProdotto(<%=prodottiVetrina.get(i).getIdProd()%>)" style="color: red;"> Visualizza</a></p>
                    </div>
                </div>
                <%}%>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselVetrina" data-bs-slide="prev" >
                <span class="carousel-control-prev-icon" ></span>
                <span class="visually-hidden" >Previous</span>
            </button>
            <button class="carousel-control-next btn-" type="button" data-bs-target="#carouselVetrina" data-bs-slide="next">
                <span class="carousel-control-next-icon" ></span>
                <span class="visually-hidden" >Next</span>
            </button>
        </div>
    </section>


    <section class="prodotti">
        <% for (int i = 0; i < prodotti.size(); i++) { %>

        <% if (i % 3 == 0) {%>
        <div style="clear: both;"></div>
        <%}%>
        <article style="float:left;">
            <div class="card" style="width: 18rem; margin: 10px;">
                <img src="uploadedImages/<%=prodotti.get(i).getIdProd()%>/<%=prodotti.get(i).getFotoProdotto()[0].getName()%>"
                     class="card-img-top" style="height: 14rem;">
                <div class="card-body">
                    <h5 class="card-title"><%= prodotti.get(i).getNomeProd().length()>20? prodotti.get(i).getNomeProd().substring(0,19)+"..." :prodotti.get(i).getNomeProd() %>
                    </h5>
                    <p class="card-text">&euro; <%= prodotti.get(i).getPrezzo()%>
                    </p>
                    <a href="javascript:viewDettagliProdotto(<%=prodotti.get(i).getIdProd()%>)" class="btn btn-primary">Visualizza</a>
                </div>
            </div>
        </article>
        <% }%>
    </section>

</main>


<div style="clear: both;"></div>

<nav aria-label="Navigation" style="margin: 0 auto; width: fit-content;">
    <ul class="pagination">
        <li class="page-item"><a class="page-link" href="javascript:viewPage(0)">Previous</a></li>
        <%--for (int i=0; i< numPagine; i++) {--%>
            <li class="page-item"><a class="page-link" href="javascript:viewPage(<%--=i--%>)"> <%--=i+1--%></a></li>
        <%--}--%>
        <li class="page-item"><a class="page-link" href="javascript:viewPage(<%--=numPagine-1--%>)">Next</a></li>
    </ul>
</nav>

<form name="selectionForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="CatalogManagement.view"/>
    <input type="hidden" name="selectedProduct" value=""/>
</form>

<form name="paginationForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
    <input type="hidden" name="paginationIndex" value="0"/>
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