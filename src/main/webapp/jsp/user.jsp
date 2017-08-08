<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Profile Page | Beingjavaguys.com</title>
<link rel="stylesheet" href="css/background/background.css" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
	
	<div class="container" >
<br>
		<div class="row">
			<div class="col-md-8">
				<h2>Welcome ${user.firstName} ${user.lastName} to Docsolr.</h2>
			</div>
			<br>
			<div class="col-lg-4">
				<form action="j_spring_security_logout" method="GET">
					<input type="submit" value="logout" class="btn btn-primary btn-md" />
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>
			</div>
		</div>
<br>
		<div class="row">
			<form method="POST" action="uploadFile" enctype="multipart/form-data">

				<div class="col-md-6">
					File to upload: <input type="file" name="file">
				</div>
<br>
				
				<div class="col-md-6">
					<input type="submit" value="Upload"> Press here to upload
					the file!
				</div>
			</form>
		</div>		
		
		
	</div>
</body>
</html>