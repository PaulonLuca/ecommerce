<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%@ page session="false" %>
<%@ page errorPage="../errorPage.jsp"%>

<%
    //jsp per la registrazione dell'utente
    //Viene fatta una validazione sui campi della form prima del submit.
    //L'indirizzo che viene richiesto è quello di residenza legato all'utente.

    boolean isAdmin =(Boolean) request.getAttribute("isAdmin");
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" href="css/ecommerce1.css" type="text/css" media="screen">
    <script>
        var applicationMessage;
        <%if (applicationMessage != null) {%>
        applicationMessage="<%=applicationMessage%>";
        <%}%>
        function backToHome()
        {document.backForm.submit();}

        function validateForm() {

            if( isNaN(document.registrationForm.telefono.value) ) {
                alert( "Il telefono deve contenere solo valori numerici" );
                document.registrationForm.telefono.focus() ;
                return false;
            }

            if( isNaN(document.registrationForm.cap.value) ) {
                alert( "Il cap deve contenere solo valori numerici" );
                document.registrationForm.cap.focus() ;
                return false;
            }

            return( true );
        }

        function onLoadHandler() {
            var btn = document.querySelector("#btnBack");
            btn.addEventListener("click", backToHome);

            if (applicationMessage!=undefined) alert(applicationMessage);
        }
        window.addEventListener("load",onLoadHandler);
    </script>
</head>
<body>

<%--@include file="/include/header.jsp"--%>
<header class="clearfix ">
    <h1 class="logo" >
        E-commerce
    </h1>
    <button type="button" name="btnBack" id="btnBack" class="button btnheader" >Home</button>
</header>


<main>
    <h1 style="text-align: center;"> Registrati inserendo tutti i campi obbligatori</h1>

    <form name="registrationForm" method="post" action="Dispatcher" onsubmit="return validateForm()" class="form-control-lg formReg" >
        <fieldset>
            <table >
                <tr>
                    <td><label for="nome">Nome</label></td>
                    <td> <input type="text" id="nome"  name="nome" maxlength="50" required></td>
                </tr>
                <tr>
                    <td><label for="cognome">Cognome</label></td>
                    <td><input type="text" id="cognome"  name="cognome" maxlength="50" required></td>
                </tr>
                <tr>
                    <td><label for="email">Email</label></td>
                    <td><input type="email" id="email"  name="email" maxlength="60" required></td>
                </tr>
                <tr>
                    <td><label for="telefono">Telefono</label></td>
                    <td><input type="text" id="telefono"  name="telefono" maxlength="40" required></td>
                </tr>
                <tr>
                    <td><label for="citta">Citt&agrave;</label></td>
                    <td><input type="text" id="citta"  name="citta" maxlength="40" required></td>
                </tr>
                <tr>
                    <td><label for="via">Via</label></td>
                    <td><input type="text" id="via"  name="via" maxlength="40" required></td>
                </tr>
                <tr>
                    <td><label for="civico">Civico</label></td>
                    <td><input type="text" id="civico"  name="civico" maxlength="4" required></td>
                </tr>
                <tr>
                    <td><label for="cap">Cap</label></td>
                    <td><input type="text" id="cap"  name="cap" maxlength="10" required></td>
                </tr>
                <tr>
                    <td><label for="username">Username</label></td>
                    <td><input type="text" id="username"  name="username" maxlength="40" required></td>
                </tr>
                <tr>
                    <td><label for="password">Password</label></td>
                    <td><input type="password" id="password"  name="password" maxlength="40" required></td>
                </tr>

            </table>
            <input type="submit" class="button" value="Registrati" style="margin-top: 15px;"/>

            <input type="hidden" name="controllerAction" value="UserManagement.add"/>
        </fieldset>
    </form>
</main>

<form name="backForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
</form>
</body>

<%@include file="/include/footer.jsp"%>

</html>