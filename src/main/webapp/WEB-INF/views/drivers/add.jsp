<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <style><%@include file='/WEB-INF/views/css/table_dark.css' %></style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
              crossorigin="anonymous">
    <title> Register </title>
  </head>
  <body class="bg-primary">
    <a class=" btn btn-light logout__btn" href="${pageContext.request.contextPath}/index"> Back </a>
    <h1 class="table_dark" style="width:53%">Register in taxi service as a driver:</h1>
    <div class="d-flex align-item-center justify-content-center">
      <h4 class="text-center" style="color:white">${errorMsg}</h4>
    </div>
    <div class="row justify-content-center">
      <div class="col-lg-5">
        <form class="py-3" method="post" id="driver" action="${pageContext.request.contextPath}/drivers/add">
          <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addition">Name:</span>
            <input type="text" class="form-control" placeholder="Driver name"name="name">
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addition">License number:</span>
            <input type="text" class="form-control" placeholder="License number" name="license">
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addition">Login:</span>
            <input type="text" class="form-control" placeholder="Login" name="login">
          </div>
          <div class="input-group mb-3">
            <span class="input-group-text" id="basic-addition">Password:</span>
            <input type="password" class="form-control" placeholder="Password" name="password">
          </div>
          <div class="input-group mb-3 center">
            <button type="submit" class="btn btn-light" form="driver">
              Register
            </button>
          </div>
        </form>
      </div>
    </div>
  </body>
</html>
