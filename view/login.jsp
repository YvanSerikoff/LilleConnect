<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
<form action="controller.LoginController" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>
    <button type="submit">Login</button>
</form>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
</body>
</html>
