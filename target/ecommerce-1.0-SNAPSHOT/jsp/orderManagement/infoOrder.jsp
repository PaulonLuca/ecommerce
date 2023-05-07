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
</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">

        <h1 style="text-align: left;"> Inserire i campi obbligatori</h1>

        <form name="orderForm" method="post" action="Dispatcher" class="form-control-lg " >
            <h2>Indirizzo di spedizione</h2>
            <fieldset>
                <table >
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
                        <td><input type="citta" id="citta"  name="email" maxlength="60" required></td>
                    </tr>
                    <tr>
                        <td><label for="cap">Cap</label></td>
                        <td><input type="text" id="cap"  name="cap" maxlength="40" required></td>
                    </tr>
                </table>
            </fieldset>

            <h2>Informazioni spedizione</h2>
            <fieldset>
                <table>
                    <tr>
                        <td><label for="spedizioneSt">Spedizione standard</label></td>
                        <td> <input type="radio" id="spedizioneSt"  name="spedizione" required></td>
                    </tr>
                    <tr>
                        <td><label for="spedizioneEsp">Spedizione espressa</label></td>
                        <td><input type="radio" id="spedizioneEsp"  name="spedizione" required></td>
                    </tr>
                </table>
            </fieldset>

            <h2> Informazioni pagamento</h2>
            <fieldset>
                <table >
                    <tr>
                        <td><label for="metodo">Metodo pagamento</label></td>
                        <td> <input type="text" id="metodo"  name="metodo"  required></td>
                    </tr>
                    <tr>
                        <td><label for="numeroCarta">Numero carta</label></td>
                        <td> <input type="text" id="numeroCarta"  name="numeroCarta" maxlength="16" required></td>
                    </tr>
                    <tr>
                        <td><label for="mese">Mese</label></td>
                        <td><input type="text" id="mese"  name="mese" maxlength="2" required></td>
                    </tr>
                    <tr>
                        <td><label for="anno">Anno</label></td>
                        <td><input type="email" id="anno"  name="email" maxlength="60" required></td>
                    </tr>
                    <tr>
                        <td><label for="cvv">CVV</label></td>
                        <td><input type="text" id="cvv"  name="cvv" maxlength="3" required></td>
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