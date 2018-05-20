<%@ page isErrorPage="true" import="java.io.*" contentType="text/plain"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
Message:
<%	if (exception != null) {
	exception.getMessage();
}
%>

<c:catch var="exception">${bufr}</c:catch>
<c:if test="!${exception}">${bufr}</c:if>	
<c:if test="${exception}">${exception}</c:if>	


BUFR: ${bufr}


StackTrace:
<%
	StringWriter stringWriter = new StringWriter();
	PrintWriter printWriter = new PrintWriter(stringWriter);
	if (exception != null) {
	   exception.printStackTrace(printWriter);
	   } else {
	   out.println("exception is null");
	   }

	out.println(stringWriter);
	printWriter.close();
	stringWriter.close();
%>





