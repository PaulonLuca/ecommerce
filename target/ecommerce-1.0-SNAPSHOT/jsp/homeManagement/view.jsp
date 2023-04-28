<%@ page import="com.unife.ecommerce.model.mo.Utente" %>
<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
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


</main>
</body>

<%@include file="/include/footer.inc"%>

</html>