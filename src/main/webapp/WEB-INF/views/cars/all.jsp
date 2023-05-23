<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <style><%@include file='/WEB-INF/views/css/table_dark.css' %></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
          crossorigin="anonymous">
    <title> All cars </title>
  </head>
  <body class="bg-primary">
    <a class=" btn btn-light logout__btn" href="${pageContext.request.contextPath}/index"> Back </a>
    <h1 class="table_dark">All cars:</h1>
    <div class="d-flex align-item-center justify-content-center">
      <h4 class="text-center" style="color:white">${errorMsg}</h4>
    </div>
    <table border="1" class="table_dark">
      <tr>
        <th>ID</th>
        <th>Model</th>
        <th>Manufacturer</th>
        <th>Manufacturer country</th>
        <th>Drivers</th>
        <th>Delete</th>
      </tr>
      <c:forEach var="car" items="${cars}">
        <tr>
            <td>
                <c:out value="${car.id}"/>
            </td>
            <td>
                <c:out value="${car.model}"/>
            </td>
            <td>
                <c:out value="${car.manufacturer.name}"/>
            </td>
            <td>
                <c:out value="${car.manufacturer.country}"/>
            </td>
            <td>
                <c:forEach var="driver" items="${car.drivers}">
                    ${driver.id} ${driver.name} ${driver.login} ${driver.licenseNumber} <br>
                </c:forEach>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/cars/delete?id=${car.id}">DELETE</a>
            </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
