<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <style><%@include file='/WEB-INF/views/css/table_dark.css' %></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
            crossorigin="anonymous">
    <title> Manage page </title>
  </head>
  <body class="bg-primary">
    <a class=" btn btn-light logout__btn" href="${pageContext.request.contextPath}/logout"> Logout </a>
    <h1 class="table_dark"> Taxi service manage page </h1>
    <br>
    <table class="table_dark">
      <tr>
        <th>
          Redirect to:
        </th>
      </tr>
      <tr><td><a href="${pageContext.request.contextPath}/drivers"> Display All Drivers </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/cars"> Display All Cars </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/manufacturers"> Display All Manufacturers </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/drivers/add"> Create new Driver </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/cars/add"> Create new Car </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/manufacturers/add"> Create new Manufacturer </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/cars/drivers/add"> Add Driver to Car </a></td></tr>
      <tr><td><a href="${pageContext.request.contextPath}/drivers/cars"> Get My Current Cars </a></td></tr>
    </table>
  </body>
</html>
