<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Docsolr</title>

</head>
<body>
	
	<div class="container" ng-controller= "UploadFileController">
	<br>
		<div class="row">
			<form>

				<div class="col-md-6">
					File to upload: <input type="file" demo-file-model="myFile"  class="form-control" id ="myFileField"/>
				</div>
<br>
				
				<div class="col-md-6">
					<button ng-click="uploadFile()" class = "btn btn-primary">Upload File</button> Press here to upload
					the file!
				</div>
			</form>
		</div>
		<br>
		<div>Content of File : <br> {{serverResponse.Contents}}</div>
		<br>
		<div>MetaData :<br> {{serverResponse.Meta}}</div>
	</div>
</body>
</html>