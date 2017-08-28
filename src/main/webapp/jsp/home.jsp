<%-- <!DOCTYPE html>
<html enterprise>
<title>Docsolr</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/homepage/home.css">
<link rel="stylesheet" href="css/homepage/hometheme.css">
<link rel="stylesheet" href="css/homepage/homefont.css">

<link rel="stylesheet" href="css/tree.css"/>
<script src="js/mainapp.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.5/angular.min.js"></script>
<script src="https://code.angularjs.org/1.3.0-beta.5/angular.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-route.js"></script>



<script src="js/app.js"></script>
<script src="js/treedirective.js"></script>

<body id="myPage" ng-app="myApp">


<!-- Navbar -->
<div class="w3-top">
 <div class="w3-bar w3-theme-d2 w3-left-align">
  <a class="w3-bar-item w3-button w3-hide-medium w3-hide-large w3-right w3-hover-white w3-theme-d2" href="javascript:void(0);" onclick="openNav()"><i class="fa fa-bars"></i></a>
  <a href="#" class="w3-bar-item w3-button w3-teal"> <span class="glyphicon glyphicon-home"></span> Docsolr</a>
  
 
    <div class="barmodule">
     <a href="#parser" class="w3-bar-item w3-button w3-hide-small w3-hover-white">Parser</a>
    <a href="#!tree" class="w3-bar-item w3-button w3-hide-small w3-hover-white">SalesdorceMetadata</a>
    <div class="w3-dropdown-hover w3-hide-small">
   
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

<!-- Image Header -->
<div class="w3-display-container w3-animate-opacity">
  <img src="/docsolr/images/blue.png" alt="boat" style="width:100%;min-height:350px;max-height:600px;">
  
</div>




<!-- Team Container -->
<div class="w3-container w3-padding-64 w3-center" id="parser">
<h2>Parsing</h2>
<p>Here you can parse your file</p>

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

<!-- Work Row -->
<div class="w3-row-padding w3-padding-64 w3-theme-l1" id="work">

<div class="w3-quarter">
<h2>Our Work</h2>
<ul class="tree">
        <node-tree children="jsonData"></node-tree>
    </ul>
    
    <button  type="submit"  value = "Submit" id="button" class="btn btn-primary btn-lg btn-block login-button" ng-click="getSelected(item)">Get Selected</button>
    
    <br/>
    <br/>
    Selected: 
    <ul>
      <li ng-repeat="item in selectedItemsObject">
        {{item.selected}}
        {{item.key}}
      </li>
    </ul>
 </div>
</div>

</body>
</html>
 --%>