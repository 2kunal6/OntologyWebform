<%@ page import ="java.util.*" %>
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
        table {border-collapse: separate;border-spacing: 50px 0;}
        td {padding: 10px 0;}
    </style>
</head>
<body>
<center>
<h1>

</h1>
<%
List result= (List) request.getAttribute("classList");
Iterator it = result.iterator();
out.println("<table>");
while(it.hasNext()){
    String val = (String)it.next();
    out.println("<tr>");
    out.println("<td>");
    out.println("<input type='text' id=" + val + "name=" + val + ">");
    out.println("</td>");
    out.println("<td>");
    out.println(" is a ");
    out.println("</td>");
    out.println("<td>");
    out.println(val);
    out.println("</td>");
    out.println("</tr>");
}
out.println("</table>");
%>
</body>
</html>