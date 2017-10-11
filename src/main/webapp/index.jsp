<!DOCTYPE html>
<html>


<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-route.js"></script>
<!-- <script src="https://code.angularjs.org/1.3.0-beta.5/angular.js"></script> -->

<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/homepage/home.css">
<link rel="stylesheet" href="css/homepage/hometheme.css">
<link rel="stylesheet" href="css/homepage/homefont.css">
<link href="css/registration/register.css" rel="stylesheet">

<body ng-app="myApp">

	<p>
		<a href="#/!">Main</a>
	</p>

	<!-- Navbar -->
	<div class="w3-top">
		<div class="w3-bar w3-theme-d2 w3-left-align">
			<a
				class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-hover-white w3-theme-d2"
				href="javascript:void(0);" onclick="openNav()"><i
				class="fa fa-bars"></i></a> <a href="#"
				class="w3-bar-item w3-button w3-manualblue"> <span
				class="glyphicon glyphicon-home"></span> Docsolr
			</a>


			<div class="barmodule">
				<a href="#!login"
					class="w3-bar-item w3-button w3-hide-small w3-hover-white">LOG
					IN</a> <a href="#!signup"
					class="w3-bar-item w3-button w3-hide-small w3-hover-white">SIGN
					UP</a>
				<div class="w3-dropdown-hover w3-hide-small"></div>
			</div>
		</div>

	
	</div>
	<br>

	<div ng-view></div>

	<script src="js/mainapp.js"></script>
	<script src="js/treedirective.js"></script>
	
	<script src="js/app.js"></script>
	<script src="js/Controllers/homecontroller.js"></script>

</body>
</html>


