<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<h1>Dashboard de ${sessionScope.user.name}</h1>

<c:if test="${not empty errorMessage}">
    <p class="error" style="color: red">${errorMessage}</p>
</c:if>


<form action="CreateThreadServlet" method="post">
    <label for="title">Title:</label>
    <input type="text" id="title" name="title" required>
    <button type="submit">Create Thread</button>
</form>
</body>
</html>