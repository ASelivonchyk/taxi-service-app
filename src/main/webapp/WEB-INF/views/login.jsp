<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
          crossorigin="anonymous">
    <title>Login page</title>
</head>
<body class="bg-primary">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-5">
          <div class="card shadow-lg border-0 rounded-lg mt-5">
            <div class="card-header">
              <h3 class="text-center my-3">Login in Taxi Service:</h3>
            </div>
              <div class="card-body">
                <form method="post" id="login" action="${pageContext.request.contextPath}/login">
                  <div class="form-floating mb-3">
                  <input class="form-control" type="text" name="login" id="login"
                       placeholder="login">
                  <label for="login">Login</label>
                </div>
                  <div class="form-floating mb-3">
                  <input class="form-control" type="password" name ="password"  id="password"
                       placeholder="password">
                  <label for="password" class="form-label">Password</label>
                  <br>
                  <div class="d-flex align-item-center justify-content-center">
                    <button class="btn btn-primary" type="submit">Login</button>
                  </div>
                </form>
              </div>
              <div class="d-flex align-item-center justify-content-center">
                <h4 class="text-center" style="color:RED">${errorMsg}</h4>
                <h4 class="text-center" style="color:black">${successMsg}</h4>
              </div>
              <div class="card-footer text-center py-3">
                <a href="${pageContext.request.contextPath}/drivers/add">
                  Don`t have account? Sign up!
                </a>
              </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
