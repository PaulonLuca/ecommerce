<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.Prodotto" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Prodotto> prodotti= (ArrayList<Prodotto>) request.getAttribute("prodotti");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
</head>
<body>
    <%@include file="/include/header.inc"%>

<main>
    <% for( int i=0; i<prodotti.size(); i++) { %> <!---1 perchè ci sono 10 prodotti per ora-->

    <% if(i%3==0) {%> <div style="clear: both;"></div> <%}%>
    <article style="float:left;">
        <div class="card" style="width: 18rem; ">
            <img src="uploadedImages/<%=prodotti.get(i).getIdProd()%>/<%=prodotti.get(i).getFotoProdotto()[0].getName()%>" class="card-img-top" alt="...">
            <div class="card-body">
                <h5 class="card-title"><%= prodotti.get(i).getNomeProd()%></h5>
                <p class="card-text">&euro; <%= prodotti.get(i).getPrezzo()%></p>
                <a href="#" class="btn btn-primary">Go somewhere</a>
            </div>
        </div>
    </article>

   <% }%>


</main>
</body>

<%@include file="/include/footer.inc"%>

</html>