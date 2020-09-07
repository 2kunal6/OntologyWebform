<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
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