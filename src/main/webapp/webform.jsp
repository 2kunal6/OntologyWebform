<%@ page import ="java.util.*" %>
<!DOCTYPE html>
<html>
<body>
<center>
<h1>

</h1>
<%
List result= (List) request.getAttribute("classList");
Iterator it = result.iterator();
while(it.hasNext()){
    String val = (String)it.next();
    out.println(val + "  <input type='text' id=" + val + "name=" + val + "><br>");
}
%>
</body>
</html>