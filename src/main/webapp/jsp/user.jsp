<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Docsolr</title>

</head>
<body>
	
	<div class="container" >
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