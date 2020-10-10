<%@ page import ="java.util.*" %>
<%@ page import ="de.unibonn.model.OntologyClass" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>OntologyWebform</title>

    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="apple-touch-icon" href="apple-touch-icon.png">

    <link rel="stylesheet" href="css/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/css/fontAwesome.css">
    <link rel="stylesheet" href="css/css/hero-slider.css">
    <link rel="stylesheet" href="css/css/templatemo-main.css">
    <link rel="stylesheet" href="css/css/owl-carousel.css">

    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800" rel="stylesheet">

    <script src="css/js/vendor/modernizr-2.8.3-respond-1.4.2.min.js"></script>
    <script type="text/javascript">
        function populateText(selectId, classTextId) {
            var e = document.getElementById(selectId);
            document.getElementById(classTextId).value = e.options[e.selectedIndex].text;
        }
    </script>
    <style>
        table {border-collapse: separate;border-spacing: 50px 0;}
        td {padding: 10px 0;}
    </style>
</head>
<body>
<center>
<h1>

</h1>
<form method="post" action="triple">
    <%
    List<OntologyClass> result= (List<OntologyClass>) request.getAttribute("ontologyClasses");
    for(OntologyClass ontologyClass : result) {
        String val = ontologyClass.getOntclass().toString();
        out.println("<label for=" + val + ">" + val + ":</label>");
        out.print("<input type='text' id='" + val + "' name='" + val + "'>");

        if(ontologyClass.getIndividuals().size()>0) {
            out.println("<select id='" + val+ "_dropdown'; onchange='populateText(\"" + val + "_dropdown\", \"" + val + "\")'>");
            out.println("<option value=\"\">None</option>");
            for(String individual : ontologyClass.getIndividuals()) {
                out.println("<option value=\"" + individual + "\">" + individual + "</option>");
            }
            out.println("</select><br/>");
        }
        out.println("<br/>");
    }
    %>
    <input type="submit">
</form>
</body>
</html>