<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Marca> marche = (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie = (ArrayList<Categoria>) request.getAttribute("categorie");
    ArrayList<IndirizzoSpedizione> indirizzi=(ArrayList<IndirizzoSpedizione>) request.getAttribute("indirizzi");
    ArrayList<TipoPagamento> tipiPag=(ArrayList<TipoPagamento>) request.getAttribute("tipiPag");
    ArrayList<Spedizione> spedizioni=(ArrayList<Spedizione>) request.getAttribute("spedizioni");
    Carrello carrello=(Carrello) request.getAttribute("carrello");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc" %>

    <script>

        function gestisciCampiIndirizzo(){
            var idTipoPag=document.querySelector('input[name="newInd"]:checked').value;
            if (idTipoPag=="new") {
                document.querySelector("#via").disabled=false;
                document.querySelector("#civico").disabled=false;
                document.querySelector("#citta").disabled=false;
                document.querySelector("#cap").disabled=false;
                document.querySelector("#indirizzo").disabled=true;
            }
            else
            {
                document.querySelector("#via").disabled=true;
                document.querySelector("#civico").disabled=true;
                document.querySelector("#citta").disabled=true;
                document.querySelector("#cap").disabled=true;
                document.querySelector("#indirizzo").disabled=false;
            }
        }

        function gestisciCampiCarta(){
            var idTipoPag=document.querySelector('input[name="tipoPag"]:checked').value;
            if (idTipoPag==2 ||idTipoPag==3) {
                document.querySelector("#numeroCarta").disabled=false;
                document.querySelector("#mese").disabled=false;
                document.querySelector("#anno").disabled=false;
                document.querySelector("#cvv").disabled=false;
            }
            else
            {
                document.querySelector("#numeroCarta").disabled=true;
                document.querySelector("#mese").disabled=true;
                document.querySelector("#anno").disabled=true;
                document.querySelector("#cvv").disabled=true;
            }
        }
    </script>
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">

        <h1 style="text-align: left;"> Inserire i campi obbligatori</h1>

        <form name="orderForm" method="post" action="Dispatcher" class="form-control-lg " >
            <h2>Indirizzo di spedizione</h2>
            <fieldset>
                <table class="table">
                    <%if(indirizzi.size()>0){%>
                    <tr>
                        <td>Nuovo indirizzo</td>
                        <td><input type="radio" id="newInd"  name="newInd" checked value="new"  required onclick="gestisciCampiIndirizzo()"></td>
                    </tr>
                    <tr>
                        <td>Utilizza esistente</td>
                        <td><input type="radio" id="oldInd"  name="newInd" value="old" required onclick="gestisciCampiIndirizzo()"></td>
                    </tr>

                    <tr>
                        <td><label for="indirizzo">Indirizzo</label></td>
                        <td> <select id="indirizzo" name="indirizzo">
                                <%for (int i=0;i<indirizzi.size();i++){%>
                                <option value="<%=indirizzi.get(i).getIdIndSped()%>">
                                    <%=indirizzi.get(i).getVia()%> <%=indirizzi.get(i).getCivico()%> <%=indirizzi.get(i).getCitta()%> <%=indirizzi.get(i).getCap()%>
                                </option>
                                <%}%>
                            </select>
                        </td>
                    </tr>
                    <%}%>

                    <tr>
                        <td><label for="via">Via</label></td>
                        <td> <input type="text" id="via"  name="via" maxlength="50" required></td>
                    </tr>
                    <tr>
                        <td><label for="civico">Civico</label></td>
                        <td><input type="text" id="civico"  name="civico" maxlength="50" required></td>
                    </tr>
                    <tr>
                        <td><label for="citta">Citt&agrave;</label></td>
                        <td><input type="citta" id="citta"  name="citta" maxlength="60" required></td>
                    </tr>
                    <tr>
                        <td><label for="cap">Cap</label></td>
                        <td><input type="text" id="cap"  name="cap" maxlength="40" required></td>
                    </tr>
                </table>
            </fieldset>

            <h2>Informazioni spedizione</h2>
            <fieldset>
                <table class="table">
                    <tr><th>Tipo spedizione</th><th>Costo</th><th>Tempo (gg)</th></tr>
                    <%for(int i=0;i<spedizioni.size();i++){%>
                    <tr>
                        <td><label for="spedizione"><%=spedizioni.get(i).getNomeSped()%></label></td>
                        <td>&euro; <%=spedizioni.get(i).getCosto()%></td>
                        <td><%=spedizioni.get(i).getNumGiorni()%></td>
                        <td> <input type="radio" id="spedizione"  name="spedizione" <%=i==0? "checked" : ""%> value="<%=spedizioni.get(i).getIdSped()%>"  required></td>
                    </tr>
                    <%}%>
                </table>
            </fieldset>

            <h2> Informazioni pagamento</h2>
            <fieldset>
                <table class="table">
                    <%for(int i=0;i<tipiPag.size();i++) {%>
                    <tr>
                        <td><label for="tipoPag"><%=tipiPag.get(i).getNomeTipoPag()%></label></td>
                        <td> <input type="radio" id="tipoPag"  name="tipoPag" <%=i==0? "checked" : ""%> value="<%=tipiPag.get(i).getIdTipoPag()%>" onclick="gestisciCampiCarta()" required></td>
                    </tr>
                    <%}%>
                    <tr>
                        <td><label for="numeroCarta">Numero carta</label></td>
                        <td> <input type="text" id="numeroCarta"  name="numeroCarta" maxlength="16" required disabled></td>
                    </tr>
                    <tr>
                        <td><label for="mese">Mese</label></td>
                        <td><input type="text" id="mese"  name="mese" maxlength="2" required disabled></td>
                    </tr>
                    <tr>
                        <td><label for="anno">Anno</label></td>
                        <td><input type="text" id="anno"  name="anno" maxlength="60" required disabled></td>
                    </tr>
                    <tr>
                        <td><label for="cvv">CVV</label></td>
                        <td><input type="text" id="cvv"  name="cvv" maxlength="3" required disabled></td>
                    </tr>
                </table>
            </fieldset>

            <input type="submit" class="button" value="Conferma ordine" style="margin-top: 15px;"/>
            <input type="hidden" name="controllerAction" value="OrderManagement.add"/>
        </form>

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