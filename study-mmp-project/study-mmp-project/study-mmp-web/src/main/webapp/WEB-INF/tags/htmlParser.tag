<%@ attribute name="value" required="true" rtexprvalue="true" %><%
value = value.replaceAll("<","&lt;");
value = value.replaceAll(">","&gt;");
value = value.replaceAll("\"","&quot;");
value = value.replaceAll("\'","&#39;");
%><%=value%>