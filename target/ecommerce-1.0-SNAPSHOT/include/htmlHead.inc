<meta charset="utf-8"/>

<!-- Linking styles -->
<link rel="stylesheet" href="css/ecommerce.css" type="text/css" media="screen">
<title>Ecommerce: <%=menuActiveLink%></title>
<script>
  var applicationMessage;
  <%if (applicationMessage != null) {%>
    applicationMessage="<%=applicationMessage%>";
  <%}%>
  function onLoadHandler() {
    headerOnLoadHandler();
    try { mainOnLoadHandler(); } catch (e) {}
    if (applicationMessage!=undefined) alert(applicationMessage);
  }
  window.addEventListener("load", onLoadHandler);
</script>