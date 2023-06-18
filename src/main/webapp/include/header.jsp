<script>
    function searchtByCategoria(code) {
        document.searchForm.selectedCat.value = code;
        document.searchForm.submit();
    }
    function searchByMarca(code) {
        document.searchForm.selectedMarca.value = code;
        document.searchForm.submit();
    }
    function searchByString() {
        document.searchForm.searchString.value = document.inputForm.inputString.value;
        if(document.searchForm.searchString.value!="")
            document.searchForm.submit();
    }

    function headerOnLoadHandler() {
        var usernameTextField = document.querySelector("#username");
        var usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
        var passwordTextField = document.querySelector("#password");
        var passwordTextFieldMsg = "La password \xE8 obbligatoria.";

        if (usernameTextField != undefined && passwordTextField != undefined) {
            usernameTextField.setCustomValidity(usernameTextFieldMsg);
            usernameTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
            });
            passwordTextField.setCustomValidity(passwordTextFieldMsg);
            passwordTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
            });
        }

        var btnSearch = document.querySelector("#btnSearch");
        btnSearch.addEventListener("click",searchByString);
    }
</script>
<!--Si discriminano le funzionalità della navbar in base alla tipologia di utente:
    1) Pubblico: può navigare tra i prodotti e registrarsi e fare logon
    2) Registrato: può fare logon, navigare tra i prodotti, vedere il carrello e gli ordini
    3) Admin: può fare logon, navigare tra i prodotti, vedere gli utenti, inseririe nuove marche e prodotti,
    e vedere gli ordini di tutti gli altri utenti-->
<header class="clearfix"><!-- Defining the header section of the page -->

    <h1 class="logo"><!-- Defining the logo element -->
        E-commerce
    </h1>

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>

    <form name="registrationForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.registrationView"/>
    </form>

    <form name="cartForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="CartManagement.view"/>
    </form>

    <%if (!loggedOn) {%>
    <section id="login" class="clearfix">
        <form name="logonForm" action="Dispatcher" method="post">
            <label for="username">Utente</label>
            <input type="text" id="username" name="username" maxlength="40" required>
            <label for="password">Password</label>
            <input type="password" id="password" name="password" maxlength="40" required>
            <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
            <input type="submit" value="Ok">
        </form>
    </section>
    <% } %>
    <div style="clear: both;"></div>

    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand"><%= loggedOn? loggedUser.getUsername(): ""%></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                    <li class=" <%=menuActiveLink.equals("Home") ? "\"active\"" : ""%> nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            Categorie
                        </a>
                        <ul class="dropdown-menu">
                            <% for (int i = 0; i < categorie.size(); i++) { %>
                            <li><a href="javascript:searchtByCategoria(<%=categorie.get(i).getIdCat()%>)"><%= categorie.get(i).getNomeCat() %>
                            </a></li>
                            <% } %>
                        </ul>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            Marche
                        </a>
                        <ul class="dropdown-menu">
                            <% for (int i = 0; i < marche.size(); i++) { %>
                            <li><a href="javascript:searchByMarca(<%=marche.get(i).getIdMarca()%>)"><%= marche.get(i).getNomeMarca() %>
                            </a></li>
                            <% } %>
                        </ul>
                    </li>

                    <%if (loggedOn) {%>

                        <%if(!isAdmin) {%>
                            <li class=" <%=menuActiveLink.equals("Ordini") ? "\"active\"" : ""%> nav-item">
                                <a  class="nav-link" href="Dispatcher?controllerAction=OrderManagement.view">Ordini</a>
                            </li>

                            <li><a class="nav-link" href="javascript:logoutForm.submit()">Logout</a></li>

                            <li>
                                <button type="button" class="btn btn-primary position-relative" onclick="javascript:cartForm.submit()">
                                    Carrello <i class="bi bi-cart"></i>
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                    <%=carrello.getComposizione().size()%>
                                    <span class="visually-hidden">Carrello</span>
                                </span>
                                </button>
                            </li>

                        <%} else {%>
                        <li class=" <%=menuActiveLink.equals("Modifica stato ordini") ? "\"active\"" : ""%> nav-item">
                            <a  class="nav-link" href="Dispatcher?controllerAction=OrderManagement.view">Modifica stato ordini</a>
                        </li>

                        <li class=" <%=menuActiveLink.equals("Visualizza utenti") ? "\"active\"" : ""%> nav-item">
                            <a  class="nav-link" href="Dispatcher?controllerAction=UserManagement.view">Visualizza utenti</a>
                        </li>

                        <li class=" <%=menuActiveLink.equals("Inserisci prodotto") ? "\"active\"" : ""%> nav-item">
                            <a  class="nav-link" href="Dispatcher?controllerAction=CatalogManagement.viewInfoProdotto">Inserisci prodotto</a>
                        </li>

                        <li class=" <%=menuActiveLink.equals("Inserisci marca o categoria") ? "\"active\"" : ""%> nav-item">
                            <a  class="nav-link" href="Dispatcher?controllerAction=CatalogManagement.viewInsertCatMarca">Inserisci marca o categoria</a>
                        </li>

                        <li><a class="nav-link" href="javascript:logoutForm.submit()">Logout</a></li>

                        <%}%>

                    <%} else {%>
                    <li class=" <%=menuActiveLink.equals("Registrazione") ? "\"active\"" : ""%> nav-item">
                        <a class="nav-link" href="javascript:registrationForm.submit()">Registrati</a>
                    </li>
                    <% }%>

                </ul>
                <form name="inputForm" class="d-flex" role="search">
                    <input id="inputString" name="inputString" class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                    <button class="btn btn-outline-success"  id="btnSearch" type="button">Search</button>
                </form>
            </div>
        </div>
    </nav>

</header>