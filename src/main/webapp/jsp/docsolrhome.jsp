<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-route.js"></script>
<link rel="stylesheet" href="css/homepage/home.css">
<link rel="stylesheet" href="css/homepage/hometheme.css">
<link rel="stylesheet" href="css/homepage/homefont.css">
<link rel="stylesheet" href="css/homepage/home_background.css">

    <link rel="stylesheet" href="css/tree.css"/>
<body ng-app="myApp">

<p><a href="#/!">Main</a></p>

<!-- Navbar -->
<div class="w3-top">
 <div class="w3-bar w3-theme-d2 w3-left-align">
  <a class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-hover-white w3-theme-d2" href="javascript:void(0);" onclick="openNav()"><i class="fa fa-bars"></i></a>
  <a href="#" class="w3-bar-item w3-button w3-manualblue"> <span class="glyphicon glyphicon-home"></span> Docsolr</a>
  
 
    <div class="barmodule">
    
     <a href="#!userview" class="w3-bar-item w3-button w3-hide-small w3-hover-white">Parser</a>
    <a href="#!treeview" class="w3-bar-item w3-button w3-hide-small w3-hover-white">SalesdorceMetadata</a>
    <a href="#" class="w3-bar-item w3-button w3-hide-small w3-hover-white">SharePoint</a>
    <a href="#" class="w3-bar-item w3-button w3-hide-small w3-hover-white">SSO</a>
    <a href="#" class="w3-bar-item w3-button w3-hide-small w3-hover-white">ShareDrive</a>
    <div class="w3-dropdown-hover w3-hide-small">
   <input id="username" name="username" type="hidden" value="${user.firstName}"/>
    <button class="w3-button" title="Notifications">${user.firstName} ${user.lastName}  <span class="glyphicon glyphicon-menu-down"></span></button>     
    <div class="w3-dropdown-content w3-card-4 w3-bar-block">
    <!--   <a href="tree" class="w3-bar-item w3-button">Tree</a> -->
      <form action="j_spring_security_logout" method="GET" id="logout_form">
      <a href="#" class="w3-bar-item w3-button" onclick="document.getElementById('logout_form').submit();">logout</a>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>
				
    </div>
  </div>
 </div>
 </div>

  <!-- Navbar on small screens -->
  <div id="navDemo" class="w3-bar-block w3-theme-d2 w3-hide w3-hide-large w3-hide-medium">
    <a href="#parser" class="w3-bar-item w3-button">Parser</a>
    <a href="#work" class="w3-bar-item w3-button">SalesdorceMetadata</a>
    <a href="#contact" class="w3-bar-item w3-button">Log out</a>
    <a href="#" class="w3-bar-item w3-button">Search</a>
  </div>
</div>

<br>


<div ng-view></div>



  <script src="js/mainapp.js"></script>
  <script src="js/treedirective.js"></script>
  <script src="js/app.js"></script>
  <script src="js/Controllers/treecontroller.js"></script>
  <script src="js/Controllers/fielddatacontroller.js"></script>
  <script src="js/Controllers/homecontroller.js"></script>



</body>
</html>
