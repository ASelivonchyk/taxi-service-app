<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <style><%@include file='/WEB-INF/views/css/table_dark.css' %></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
          crossorigin="anonymous">
    <title> Add driver to car </title>
  </head>
  <body class="bg-primary">
    <a class=" btn btn-light logout__btn" href="${pageContext.request.contextPath}/index"> Back </a>
    <h1 class="table_dark">Add driver to car</h1>
    <div class="d-flex align-item-center justify-content-center">
      <h4 class="text-center" style="color:white">${errorMsg}</h4>
      <h4 style="color:black">${successMsg}</h4>
    </div>
    <form method="post" id="car" action="${pageContext.request.contextPath}/cars/drivers/add">
      <table border="1" class="table_dark">
        <tr>
          <th>Car ID</th>
          <th>Driver ID</th>
          <th>Add</th>
        </tr>
        <tr>
          <td>
            <input type="number" name="car_id" form="car" required>
          </td>
          <td>
            <input type="number" name="driver_id" form="car" required>
          </td>
          <td>
            <input type="submit" name="add" form="car">
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>
