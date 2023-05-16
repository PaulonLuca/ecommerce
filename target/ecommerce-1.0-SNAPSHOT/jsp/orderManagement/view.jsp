<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Marca> marche = (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie = (ArrayList<Categoria>) request.getAttribute("categorie");
    Carrello carrello=(Carrello) request.getAttribute("carrello");
    ArrayList<Ordine> ordini=(ArrayList<Ordine>) request.getAttribute("ordini");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp" %>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Id ordine</th>
                <th scope="col">Data</th>
                <th scope="col">Numero prodotti</th>
                <th scope="col">Lista prodotti</th>
                <th scope="col">Tipo pagamento</th>
                <th scope="col">Stato</th>
                <th scope="col">Spedizione</th>
                <th scope="col">Indirizzo</th>
                <th scope="col">Totale</th>
            </tr>
            </thead>
            <tbody>
            <% for(int i=0; i<ordini.size();i++) {%>
            <tr>
                <th><%=ordini.get(i).getIdOrd()%></th>
                <th><%=ordini.get(i).getDataOrd()%></th>
                <th>Da riempire<%--=ordini.get(i).getProdQty().size()--%></th>
                <th>
                    Da riempire con i prodotti
                    <%--
                        ArrayList<ProdottoQty> composizione=ordini.get(i).getProdQty();
                        for(int j=0;j<composizione.size();j++) {--%>
                            <%--=composizione.get(j).getProd().getNomeProd()--%>
                        <%--}--%>
                </th>
                <th><%=ordini.get(i).getPag().getTipoPag().getNomeTipoPag()%></th>
                <th><%=ordini.get(i).getStato().getNomeStato()%></th>
                <th><%=ordini.get(i).getSped().getNomeSped()%></th>
                <th><%=ordini.get(i).getIndSped().getVia()%> <%=ordini.get(i).getIndSped().getCivico()%> <%=ordini.get(i).getIndSped().getCitta()%> <%=ordini.get(i).getIndSped().getCap()%></th>
                <th> &euro; <%=ordini.get(i).getPag().getTotale()%></th>
            </tr>
            <%}%>
            </tbody>
        </table>
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