<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%@ page import="com.unife.ecommerce.model.mo.Marca" %>
<%@ page import="com.unife.ecommerce.model.mo.Categoria" %>
<%@ page import="java.util.ArrayList" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    ArrayList<Marca> marche= (ArrayList<Marca>) request.getAttribute("marche");
    ArrayList<Categoria> categorie= (ArrayList<Categoria>) request.getAttribute("categorie");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Registrazione";
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>

    <script>
        function backToHome()
        {document.backForm.submit();}

        function onLoadHandler() {
            var btn = document.querySelector("#btnBack");
            btn.addEventListener("click", backToHome);
        }
        window.addEventListener("load",onLoadHandler);

    </script>
</head>
<body>

<%--@include file="/include/header.jsp"--%>
<header class="clearfix ">
    <h1 class="logo" style="margin-bottom: 20px;">
        E-commerce
    </h1>
</header>

<button type="button" name="btnBack" id="btnBack" class="button">Home</button>

<main>
    <h1> Registrati inserendo tutti i campi obbligatori</h1>

    <form name="registrationForm" method="post" action="Dispatcher">
        <p>
            <label for="nome">Nome</label>
            <input type="text" id="nome"  name="nome" maxlength="50" required>
        </p>
        <p>
            <label for="cognome">Cognome</label>
            <input type="text" id="cognome"  name="cognome" maxlength="50" required>
        </p>
        <p>
            <label for="email">Email</label>
            <input type="email" id="email"  name="email" maxlength="60" required>
        </p>
        <p>
            <label for="telefono">Telefono</label>
            <input type="text" id="telefono"  name="telefono" maxlength="40" required>
        </p>
        <p>
            <label for="citta">Città</label>
            <input type="text" id="citta"  name="citta" maxlength="40" required>
        </p>
        <p>
            <label for="via">Via</label>
            <input type="text" id="via"  name="via" maxlength="40" required>
        </p>
        <p>
            <label for="civico">Civico</label>
            <input type="text" id="civico"  name="civico" maxlength="4" required>
        </p>
        <p>
            <label for="cap">Cap</label>
            <input type="text" id="cap"  name="cap" maxlength="10" required>
        </p>

        <p>
            <label for="username">Username</label>
            <input type="text" id="username"  name="username" maxlength="40" required>
        </p>
        <p>
            <label for="password">Password</label>
            <input type="password" id="password"  name="password" maxlength="40" required>
        </p>
        <input type="submit" class="button" value="Registrati"/>

        <input type="hidden" name="controllerAction" value="UserManagement.add"/>
    </form>

</main>

<form name="backForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
</form>
</body>

<%@include file="/include/footer.inc"%>

</html>