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
            var textIdVal = document.getElementById(classTextId).value;
            if(textIdVal)document.getElementById(classTextId).value += (", " + e.options[e.selectedIndex].text);
            else document.getElementById(classTextId).value = e.options[e.selectedIndex].text;
        }
    </script>
    <style>
        table, th, td {border: 1px solid black;border-collapse: collapse;}
        input[type="text"] {width: 500px;}
        select {width: 500px;}
    </style>
</head>
<body>
    <center>
    <br/><br/><br/>
    <h1>Individuals</h1>
    <p style="font-size:12px;">Please specify an individual or comma-separated individuals in text boxes for corresponding classes.</p>
    <p style="font-size:12px;margin-top:-10px;">Please note that you need to create individuals in this page to use them to create relations in the next page.</p>
    <p>--------------------------------------------------------------------------------------------------------------------------------</p>
    <br/><br/><br/>
<form method="post" action="triple">
    <%
    List<OntologyClass> result= (List<OntologyClass>) request.getAttribute("ontologyClasses");
    out.println("<table><tr><th>CLASS</th><th>INDIVIDUALS</th><th>SUGGESTIONS</th></tr>");
    for(OntologyClass ontologyClass : result) {
        out.println("<tr><td>");
        String val = ontologyClass.getOntclass().toString();
        String displayLabel = val;
        if(ontologyClass.getOntclass().getLabel(null)!=null)displayLabel = ontologyClass.getOntclass().getLabel(null).toString();
        out.println("<label for=" + val + ">" + displayLabel + ":</label>");
        out.println("</td><td>");
        out.print("<input type='text' id='" + val + "' name='" + val + "'>");
        out.println("</td><td>");

        if(ontologyClass.getIndividuals().size()>0) {
            out.println("<select id='" + val+ "_dropdown'; onchange='populateText(\"" + val + "_dropdown\", \"" + val + "\")'>");
            out.println("<option value=\"\">None</option>");
            for(String individual : ontologyClass.getIndividuals()) {
                out.println("<option value=" + individual + ">" + individual + "</option>");
            }
            out.println("</select>");
        }
        out.println("</td></tr>");
    }
    out.println("</table>");
    %>
    <br/><br/>
    <input type="submit">
    <br/><br/>
    <br/><br/>
</form>
</body>
</html>