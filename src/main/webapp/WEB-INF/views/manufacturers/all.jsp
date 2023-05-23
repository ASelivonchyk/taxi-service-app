<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <style>
      <%@include file='/WEB-INF/views/css/table_dark.css' %>
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
          crossorigin="anonymous">
    <title> All manufacturers </title>
  </head>
  <body class="bg-primary">
    <a class=" btn btn-light logout__btn" href="${pageContext.request.contextPath}/index"> Back </a>
    <h1 class="table_dark">All manufacturers</h1>
    <div class="d-flex align-item-center justify-content-center">
      <h4 class="text-center" style="color:white">${errorMsg}</h4>
    </div>
    <table border="1" class="table_dark">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>License number</th>
        <th>Delete</th>
      </tr>
      <c:forEach var="manufacturer" items="${manufacturers}">
        <tr>
          <td>
            <c:out value="${manufacturer.id}"/>
          </td>
          <td>
            <c:out value="${manufacturer.name}"/>
          </td>
          <td>
            <c:out value="${manufacturer.country}"/>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/manufacturers/delete?id=${manufacturer.id}">DELETE</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
