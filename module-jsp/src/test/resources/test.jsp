<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>JSP Test Page</title>
  </head>
  <body>
    <p>1 + 2 = ${ 1 + 2 }</p>
    <c:if test="${ param['render'] == 'true' }">
      Conditional content rendered
    </c:if>
  </body>
</html>