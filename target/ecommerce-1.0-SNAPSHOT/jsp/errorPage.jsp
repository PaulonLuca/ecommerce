<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%@ page session="false" %>
<%@ page isErrorPage="true"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <link rel="stylesheet" href="css/ecommerce1.css" type="text/css" media="screen">
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

<header class="clearfix ">
    <h1 class="logo" >
        E-commerce
    </h1>
    <button type="button" name="btnBack" id="btnBack" class="button btnheader" >Home</button>
</header>

<main>
    <h1>Attenzione</h1>
    <h3>Rilevato l'errore seguente</h3>
    <%= exception%><br/>
    <% exception.printStackTrace();%>

</main>

<form name="backForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
</form>
</body>

<%@include file="/include/footer.jsp"%>

</html>