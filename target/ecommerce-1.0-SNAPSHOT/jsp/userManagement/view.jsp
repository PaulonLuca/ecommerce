<%@ page import="java.util.ArrayList" %>
<%@ page import="com.unife.ecommerce.model.mo.*" %>
<%@ page session="false" %>
<%@ page errorPage="../errorPage.jsp"%>

<%
    //jsp per la visualizzazione degli utenti in modalità admin.
    //Viene visualizzata una jsp divisa in due sezioni:
    //1) Utenti non admin: è possibile bloccare/sbloccare l'utente con l'apposito pulsante, è possibile
    //rendere l'utente un admin. Al successivo logon quindi riceverà le autorizzazioni di un utente admin.
    //2) Utente admin: si visualizzano gli utenti admin, tranne quello loggato.
    //Le operazioni di modifica usano dei metodi javascipt che richiamno la funzione updateField in backend.

    ArrayList<Utente> utentiRegistrati =(ArrayList<Utente>) request.getAttribute("utentiRegistrati");
    ArrayList<Utente> utentiAdmin =(ArrayList<Utente>) request.getAttribute("utentiAdmin");
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
    <script>
        function lockUser(codeUser) {
            document.modifyUserForm.selectedUser.value = codeUser;
            document.modifyUserForm.modifiedField.value = "is_locked";
            document.modifyUserForm.modifiedFieldValue.value = true;
            document.modifyUserForm.controllerAction.value = "UserManagement.update";
            document.modifyUserForm.submit();
        }

        function unlockUser(codeUser) {
            document.modifyUserForm.selectedUser.value = codeUser;
            document.modifyUserForm.modifiedField.value = "is_locked";
            document.modifyUserForm.modifiedFieldValue.value = false;
            document.modifyUserForm.controllerAction.value = "UserManagement.update";
            document.modifyUserForm.submit();
        }

        function setAdminUser(codeUser) {
            document.modifyUserForm.selectedUser.value = codeUser;
            document.modifyUserForm.modifiedField.value = "is_admin";
            document.modifyUserForm.modifiedFieldValue.value = true;
            document.modifyUserForm.controllerAction.value = "UserManagement.update";
            document.modifyUserForm.submit();
        }

        function unsetAdminUser(codeUser) {
            document.modifyUserForm.selectedUser.value = codeUser;
            document.modifyUserForm.modifiedField.value = "is_admin";
            document.modifyUserForm.modifiedFieldValue.value = false;
            document.modifyUserForm.controllerAction.value = "UserManagement.update";
            document.modifyUserForm.submit();
        }
    </script>

</head>
<body>
<%@include file="/include/header.jsp" %>

<main class="clearfix">

    <section style="margin-top: 40px;">
        <h2>Utenti registrati</h2>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Id utente</th>
                <th scope="col">Nome</th>
                <th scope="col">Cognome</th>
                <th scope="col">Email</th>
                <th scope="col">Username</th>
                <th scope="col">Telefono</th>
                <th scope="col">Indirizzo residenza</th>
                <th scope="col">Numero ordini</th>
                <th scope="col">Bloccato</th>
                <th scope="col">Azione</th>
                <th scope="col">Privilegio</th>
            </tr>
            </thead>
            <tbody>
            <%for(int i=0;i<utentiRegistrati.size();i++){
            Utente current=utentiRegistrati.get(i); %>

                <tr>
                    <th><%=current.getIdUtente()%></th>
                    <th><%=current.getNome()%></th>
                    <th><%=current.getCognome()%></th>
                    <th><%=current.getEmail()%></th>
                    <th><%=current.getUsername()%></th>
                    <th><%=current.getTel()%></th>
                    <th><%=current.getVia()%> <%=current.getCivico()%> <%=current.getCitta()%> <%=current.getCap()%></th>
                    <th><%=current.getOrdini().size()%></th>
                    <th><%=current.isLocked()? "Bloccato" : "NON bloccato"%></th>
                    <th>
                        <%if(!current.isLocked()){%>
                            <input type="button" class="btn btn-primary" id="btnLock" nome="btnLock" value="Blocca" onclick="lockUser(<%=current.getIdUtente()%>)">
                        <%} else {%>
                            <input type="button" class="btn btn-primary" id="btnUnlock" nome="btnUnlock" value="Sblocca" onclick="unlockUser(<%=current.getIdUtente()%>)">
                        <%}%>
                    </th>
                    <th>
                        <input type="button" class="btn btn-primary" id="btnSetAdmin" nome="btnSetAdmin" value="Admin" onclick="setAdminUser(<%=current.getIdUtente()%>)">
                    </th>
                </tr>
            <%}%>
            </tbody>
        </table>
    </section>


    <section style="margin-top: 40px;">
        <h2>Utenti amministratori</h2>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Id utente</th>
                <th scope="col">Nome</th>
                <th scope="col">Cognome</th>
                <th scope="col">Email</th>
                <th scope="col">Username</th>
                <th scope="col">Telefono</th>
                <th scope="col">Indirizzo residenza</th>
                <th scope="col">Bloccato</th>
                <th scope="col">Azione</th>
                <th scope="col">Privilegio</th>
            </tr>
            </thead>
            <tbody>
            <%for(int i=0;i<utentiAdmin.size();i++){
                Utente current=utentiAdmin.get(i); %>

            <tr>
                <th><%=current.getIdUtente()%></th>
                <th><%=current.getNome()%></th>
                <th><%=current.getCognome()%></th>
                <th><%=current.getEmail()%></th>
                <th><%=current.getUsername()%></th>
                <th><%=current.getTel()%></th>
                <th><%=current.getVia()%> <%=current.getCivico()%> <%=current.getCitta()%> <%=current.getCap()%></th>
                <th><%=current.isLocked()? "Bloccato" : "NON bloccato"%></th>
                <th>
                    <%if(!current.isLocked()){%>
                    <input type="button" class="btn btn-primary" id="btnLock1" nome="btnLock1" value="Blocca" onclick="lockUser(<%=current.getIdUtente()%>)">
                    <%} else {%>
                    <input type="button" class="btn btn-primary" id="btnUnlock1" nome="btnUnlock1" value="Sblocca" onclick="unlockUser(<%=current.getIdUtente()%>)">
                    <%}%>
                </th>
                <th>
                    <input type="button" class="btn btn-primary" id="btnUnsetAdmin" nome="btnUnsetAdmin" value="NON admin" onclick="unsetAdminUser(<%=current.getIdUtente()%>)">
                </th>
            </tr>
            <%}%>
            </tbody>
        </table>
    </section>

</main>


<form name="modifyUserForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value=""/>
    <input type="hidden" name="selectedUser" value=""/>
    <input type="hidden" name="modifiedField" value=""/>
    <input type="hidden" name="modifiedFieldValue" value=""/>
</form>


<form name="searchForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.view"/>
    <input type="hidden" name="selectedCat" value=""/>
    <input type="hidden" name="selectedMarca" value=""/>
    <input type="hidden" name="searchString" value=""/>
</form>

</body>

<%@include file="/include/footer.jsp" %>

</html>