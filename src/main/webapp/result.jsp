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
</head>
<body>
<center>
<h1>
    Test header
</h1>
<%
String result= (String) request.getAttribute("retVal");
out.println("<br>We have <br><br>");
out.println(result+"<br>");
%>
</body>
</html>