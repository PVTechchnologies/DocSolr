<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Profile Page | Beingjavaguys.com</title>
</head>
<body>
	<center>
		<br /> <br /> <br />
		<h1>User profile page !!!</h1>
		
		<form action="j_spring_security_logout" method="post">
			<input type="submit" value="Log out" /> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	</center>

</body>
</html>