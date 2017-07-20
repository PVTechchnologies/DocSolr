<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Profile Page | Beingjavaguys.com</title>
</head>
<body align="center">
	
		<br /> <br /> <br />
		<h1>User profile page !!!</h1>
		
		<form action="j_spring_security_logout" method="GET">
			<input type="submit" value="logout" /> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
	
		<form method="POST" action="uploadFile" enctype="multipart/form-data">
				File to upload: <input type="file" name="file">
				<input type="submit" value="Upload"> Press here to upload the file!
		</form>	
	
</body>
</html>