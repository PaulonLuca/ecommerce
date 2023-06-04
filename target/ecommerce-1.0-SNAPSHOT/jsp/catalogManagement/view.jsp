<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%@ page session="false" %>
<%@ page errorPage="../errorPage.jsp"%>
<%
    boolean isAdmin =(Boolean) request.getAttribute("isAdmin");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Marca> marche = (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie = (ArrayList<Categoria>) request.getAttribute("categorie");
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
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

            if( isNaN(document.addToCartForm.qty.value) ) {
                alert( "Il campo quantit\u00E0 deve contenere solo valori numerici" );
                document.addToCartForm.qty.focus() ;
                return false;
            }

            if( document.addToCartForm.qty.value=="" ) {
                alert( "Il campo quantit\u00E0 non pu\u00F2 essere vuoto" );
                document.addToCartForm.qty.focus() ;
                return false;
            }

            return( true );
        }
    </script>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section class="carouselDettaglio">
        <div id="carouselProduct" class="carousel slide carousel-dark" data-bs-ride="carousel">
            <div class="carousel-indicators">
                <button type="button" data-bs-target="#carouselProduct" data-bs-slide-to="0" class="active"
                        aria-current="true" aria-label="Slide 1"></button>
                <% for (int i = 1; i < prodotto.getFotoProdotto().length; i++) { %>
                <button type="button" data-bs-target="#carouselProduct" data-bs-slide-to="<%= i%>"
                        aria-label="Slide <%= i+1%>"></button>
                <%}%>
            </div>
            <div class="carousel-inner">
                <% for (int i = 0; i < prodotto.getFotoProdotto().length; i++) { %>
                <div class="carousel-item active" data-bs-interval="1700">
                    <img src="<%=prodotto.getFotoPath()%><%=prodotto.getFotoProdotto()[i].getName()%>"
                         class="d-block imgDettaglio">
                </div>
                <%}%>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselProduct" data-bs-slide="prev">
                <span class="carousel-control-prev-icon"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next btn-" type="button" data-bs-target="#carouselProduct"
                    data-bs-slide="next">
                <span class="carousel-control-next-icon"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
    </section>


    <section class="priceDetailSection">
        <h3>&euro; <%= prodotto.getPrezzo()%>
        </h3>
        <%if(loggedOn && !isAdmin) {%>
        <form name="addToCartForm" action="Dispatcher" method="post" onsubmit="return validateForm()">
            <label for="qty"> Quantit&agrave;</label>
            <input type="number" name="qty" id="qty" value="1" min="1" max="<%=prodotto.getQtyDisp()%>"/>
            <br/>
            <input type="submit" name="conferma" id="conferma" value="Aggiungi al carrello" class="btn btn-primary"
                   style="margin-top: 6px;"/>
            <input type="hidden" name="selectedProduct" value="<%=prodotto.getIdProd()%>"/>
            <input type="hidden" name="controllerAction" value="CartManagement.add"/>
        </form>
        <%}%>
    </section>

    <div style="clear: left;"></div>

    <section style="margin-top: 40px;">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Caratteristica</th>
                <th scope="col">Dettaglio</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Nome</td>
                <td><%=prodotto.getNomeProd()%></td>
            </tr>
            <tr>
                <td>Quantit&agrave; disponibile</td>
                <td><%=prodotto.getQtyDisp()%></td>
            </tr>
            <tr>
                <td>Categoria</td>
                <td><%=prodotto.getCat().getNomeCat()%></td>
            </tr>
            <tr>
                <td>Marca</td>
                <td><%=prodotto.getMarca().getNomeMarca()%></td>
            </tr>
            <tr>
                <td>Fornitori</td>
                <td><% for(int i=0;i<prodotto.getFornitori().size();i++) {%>
                    <%=prodotto.getFornitori().get(i).getNomeFornitore() %>
                    <%}%>
                </td>
            </tr>
            </tbody>
        </table>

        <h3 style="margin-top: 40px;">Dettagli tecnici:</h3>
        <p style="text-align: justify;"><%=prodotto.getDescr()%></p>

    </section>
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