<%@ page import ="java.util.*" %>
<%@ page import ="de.unibonn.model.OntologyClass" %>
<%@ page import ="de.unibonn.model.OntologyClassRestriction" %>
<%@ page import ="org.apache.jena.ontology.OntProperty" %>
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
    <style>
        table, th, td {border: 1px solid black;border-collapse: collapse;}
    </style>
</head>
<body>
<center>
<br/><br/><br/>
    <h1>Triples</h1>
    <p style="font-size:12px;">Please specify the objects as a comma separated list for the corresponding class and property in the text boxes.</p>
    <p style="font-size:12px;margin-top:-10px;">If objects must have some property then those warnings are displayed in red.</p>
    <p>--------------------------------------------------------------------------------------------------------------------------------</p>
    <br/><br/><br/>
<form method="post" action="storeTriple">
    <%
    String validation = (String) request.getAttribute("validation");
    if(validation!=null && !validation.equals(""))out.println(validation);

    List<OntologyClass> result= (List<OntologyClass>) request.getAttribute("ontologyClasses");
    for(OntologyClass ontologyClass : result) {
        String val = ontologyClass.getOntclass().toString();
        out.println("<table><tr><th>");
        out.println("<label for=" + val + ">" + val + ":</label>");
        out.print("<input type='text' id='" + val + "' name='" + val + "'>");
        out.println("</th><th>[Predicate Object Suggestions]</th></tr>");
        for(OntProperty ontProperty : ontologyClass.getProperties()) {
            out.println("<tr><td></td><td>");
            String property = ontProperty.toString();
            out.println("<label for=" + property + ">" + property + ":</label>");
            out.println("<input type='text' id='" + property + "' name='" + val + "_XXX_CLASS_PROPERTY_SEPARATOR_XXX_" + property + "'>");

            List<OntologyClassRestriction> ontologyClassRestrictions = ontologyClass.getRestrictions();
            List<String> propertyRestrictionIndividuals=new ArrayList<String>();
            String description = "";
            for(OntologyClassRestriction ontologyClassRestriction : ontologyClassRestrictions) {
                if(ontologyClassRestriction.getOntProperty()!=null && ontologyClassRestriction.getOntProperty().toString().equals(property)) {
                    propertyRestrictionIndividuals = ontologyClassRestriction.getIndividuals();
                    description = ontologyClassRestriction.getDescription();
                    break;
                }
            }
            if(propertyRestrictionIndividuals!=null && propertyRestrictionIndividuals.size()>0) {
                out.println("<select id='" + val+ "_dropdown'; onchange='populateText(\"" + val + "_dropdown\", \"" + val + "\")'>");
                out.println("<option value=\"\">None</option>");
                for(String propertyRestrictionIndividual : propertyRestrictionIndividuals) {
                    out.println("<option value=" + propertyRestrictionIndividual + ">" + propertyRestrictionIndividual + "</option>");
                }
                out.println("</select>");
            }

            out.println("<p style='color:#931A00;'>" + description + "</p>");
            out.println("</tr></td>");
        }
        out.println("</table>");
        out.println("<br/><br/>");
    }
    %>
    <br/><br/>
    <input type="submit">
    <br/><br/>
</form>
</body>
</html>