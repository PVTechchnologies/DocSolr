<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>

<html lang="en">
    <head> 
		<meta name="viewport" content="width=device-width, initial-scale=1">


		<!-- Website CSS style -->
		<link href="css/registration/register.css" rel="stylesheet">
	<!-- 
	
		<!-- Website Font style -->
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">
		<!-- <link rel="stylesheet" href="style.css"> -->
		<!-- Google Fonts -->
		<link href='https://fonts.googleapis.com/css?family=Passion+One' rel='stylesheet' type='text/css'>
		<link href='https://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>

		<title>Docsolr</title>
		
	<!-- 	<style>
		body, html{
     height: 100%;
 	background-repeat: no-repeat;
 	background:url(https://i.ytimg.com/vi/4kfXjatgeEU/maxresdefault.jpg);
 	font-family: 'Oxygen', sans-serif;
	    background-size: cover;
}

		</style> -->
		 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  	
  	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<!--   	<script type="text/javascript" src="js/app.js"></script> -->
  	<script type="text/javascript" src="js/userlogindropdown.js"></script>
  	<%-- <spring:url value="/js/app.js" var="appJsUrl" htmlEscape="true" /> --%>
  	<script src="${appJsUrl}"></script>
	</head>
	<body ng-app="MyApp" ng-controller="UserloginController"> 
		<div class="container" >
			<div class="row main">
				<div class="main-login main-center">
				<h2>Log In</h2><br>

				<c:if test="${param.error eq 'fail'}">
					<div class="errorblock">
						Your login attempt was not successful, try again.<br /> Caused :
						${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
					</div>
				</c:if>
				
				<form class="" method="post"  name="f" action="j_spring_security_check">
					
						<div class="form-group">
							<label for="email" class="cols-sm-2 control-label">Your Email</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-envelope fa" aria-hidden="true"></i></span>
									<input type="text" class="form-control" name="username" id="email"  placeholder="Enter your Email"/>
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="corporate" class="cols-sm-2 control-label">Corporate ID</label>
						 	<div class="cols-sm-10" ng-init="getCorporateList()"> 
							<div class="input-group">
							<span class="input-group-addon"><i class="fa fa-home fa" aria-hidden="true"></i></span>
							
								<select class="form-control" id="val" >
								
        							<option value="" label="Select any Company"></option>
        							<option ng-repeat="val in CompanyList.data" value="val.id">{{val.name}}</option>
    							</select>
    						</div>	
							</div>
						</div>
						
						<div class="form-group">
							<label for="password" class="cols-sm-2 control-label">Password</label>
							<div class="cols-sm-10">
								<div class="input-group">
									<span class="input-group-addon"><i class="fa fa-key fa" aria-hidden="true"></i></span>
									<input type="password" class="form-control" name="password" id="password"  placeholder="Enter your Password"/>
								</div>
							</div>
						</div>

						
						
						<div class="form-group ">
							<button type="submit"  value = "Submit" type="button" id="button" class="btn btn-primary btn-lg btn-block login-button">Login</button>
						</div>
						
						
						
					</form>	
					<form action="connect/facebook" method="POST" class="ng-pristine ng-valid">
							<input type="hidden" name="scope" value="public_profile,email" />
							<button class="btn btn-lg btn-block kpx_btn-facebook" type="submit"
								data-toggle="tooltip" data-placement="top" title="Facebook">
								<i class="fa fa-facebook fa-2x"></i> <span class="hidden-xs"></span>
							</button>
					</form>
					
					</br>
					
					
					<form action="auth/salesforce" method="POST">
							<input type="hidden" name="scope" value="public_profile,email" />
							<button class="btn btn-lg btn-block kpx_btn-facebook" type="submit"
								data-toggle="tooltip" data-placement="top" title="Facebook">
								<i class="fa fa-Salesforce fa-2x">Salesforce</i> <span class="hidden-xs"></span>
							</button>
					</form>
					
					
				</div>
			</div>
		</div>
 
	</body>
</html>