<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%
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

</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <h2>Inserimento categoria</h2>

    <form name="categoriaForm" method="post" action="Dispatcher" class="form-control-lg formReg">
        <fieldset>
            <table >
                <tr>
                    <td><label for="categoria">Nome categoria</label></td>
                    <td> <input type="text" id="categoria"  name="categoria" maxlength="100" required></td>
                </tr>
            </table>
            <input type="submit" class="button" value="Inserisci categoria" style="margin-top: 15px;"/>
            <input type="hidden" name="controllerAction" value="CatalogManagement.addCategoria"/>
        </fieldset>
    </form>

    <h2>Inserimento marca</h2>

    <form name="marcaForm" method="post" action="Dispatcher" class="form-control-lg formReg">
        <fieldset>
            <table >
                <tr>
                    <td><label for="marca">Nome marca</label></td>
                    <td> <input type="text" id="marca"  name="marca" maxlength="100" required></td>
                </tr>
            </table>
            <input type="submit" class="button" value="Inserisci marca" style="margin-top: 15px;"/>
            <input type="hidden" name="controllerAction" value="CatalogManagement.addMarca"/>
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