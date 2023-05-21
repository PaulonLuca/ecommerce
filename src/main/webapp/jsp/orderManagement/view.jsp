<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
    ArrayList<Stato> stati =(ArrayList<Stato>) request.getAttribute("stati");
    boolean isAdmin =(Boolean) request.getAttribute("isAdmin");
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
    <script>
        <%if(isAdmin){%>
        function modifyOrder(codeOrder,codeStato) {
            document.modifyOrderForm.selectedOrder.value = codeOrder;
            document.modifyOrderForm.newStato.value = codeStato;
            document.modifyOrderForm.controllerAction.value = "OrderManagement.updateOrderState";
            document.modifyOrderForm.submit();
        }
        <%}%>
    </script>

</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">
        <table class="table">
            <thead>
            <tr>
                <%if(isAdmin){%>
                <th scope="col">Username</th>
                <%}%>
                <th scope="col">Id ordine</th>
                <th scope="col">Data</th>
                <th scope="col">Numero prodotti</th>
                <th scope="col">Lista prodotti</th>
                <th scope="col">Tipo pagamento</th>
                <th scope="col">Stato</th>
                <th scope="col">Spedizione</th>
                <th scope="col">Indirizzo</th>
                <th scope="col">Totale</th>
                <%if(isAdmin){%>
                    <th scope="col">Modifica</th>
                <%}%>
            </tr>
            </thead>
            <tbody>
            <% for(int i=0; i<ordini.size();i++) {%>
            <tr>
                <%if(isAdmin){%>
                <th ><%=ordini.get(i).getUtente().getUsername()%></th>
                <%}%>
                <th><%=ordini.get(i).getIdOrd()%></th>
                <th><%=ordini.get(i).getDataOrd()%></th>
                <th><%=ordini.get(i).getProdQty().size()%></th>
                <th>
                    <%
                        ArrayList<ProdottoQty> composizione=ordini.get(i).getProdQty();
                        for(int j=0;j<composizione.size();j++) {%>
                            <%=composizione.get(j).getProd().getNomeProd()%>
                        <%}%>
                </th>
                <th><%=ordini.get(i).getPag().getTipoPag().getNomeTipoPag()%></th>
                <th><%=ordini.get(i).getStato().getNomeStato()%></th>
                <th><%=ordini.get(i).getSped().getNomeSped()%></th>
                <th><%=ordini.get(i).getIndSped().getVia()%> <%=ordini.get(i).getIndSped().getCivico()%> <%=ordini.get(i).getIndSped().getCitta()%> <%=ordini.get(i).getIndSped().getCap()%></th>
                <th> &euro; <%=ordini.get(i).getPag().getTotale()%></th>
                <%if(isAdmin){%>
                <th>
                    <select id="stato" name="stato" onchange="modifyOrder(<%=ordini.get(i).getIdOrd()%>,value)">
                        <%for (int j=0;j<stati.size();j++){%>
                        <option value="<%=stati.get(j).getIdStato()%>" >
                            <%=stati.get(j).getNomeStato()%>
                        </option>
                        <%}%>
                    </select>
                </th>
                <%}%>
            </tr>
            <%}%>
            </tbody>
        </table>
    </section>
</main>

<%if(isAdmin){%>
<form name="modifyOrderForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value=""/>
    <input type="hidden" name="selectedOrder" value=""/>
    <input type="hidden" name="newStato" value=""/>
</form>

<%}%>

<form name="searchForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
    <input type="hidden" name="selectedCat" value=""/>
    <input type="hidden" name="selectedMarca" value=""/>
    <input type="hidden" name="searchString" value=""/>
</form>

</body>

<%@include file="/include/footer.jsp" %>

</html>