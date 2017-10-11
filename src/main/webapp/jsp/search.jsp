<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html ng-app="myApp" lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">


<!-- Website CSS style -->
<link href="css/searchpage.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css">

</head>
<body  >
<div ng-controller="SolrCtrl">
	<div class="jumbotron"  >
		<h3>Docsolr</h3>
		<div class=row>
			<div class="col-md-2">
				<span style="font-size: 30px; cursor: pointer" onclick="openNav()">&#9776;</span>
			</div>
			
			<div class="col-md-9">

				<div class="inner-addon right-addon" >
					<i class="glyphicon glyphicon-search" ng-click="solrRespnseData()"></i><input type="search"
						class="form-control" placeholder="Search" ng-model="inSearch" />
				</div>
			</div>
			
		</div>
	</div>

	<div ng-repeat="company in CompanyList">
					<br> <br>

						<table align=center>
							<tr>
								<td><a href="https://ap5.salesforce.com/{{company.id}}" style="color:white;">{{company.name}}</a></td>
							</tr>
						</table>
	</div>

		
			</div>	
				
	<div id="mySidenav" class="sidenav">
		<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
		<div class="inner-addon left-addon">
		<i class="fa fa-paperclip"></i>
		<label for="checkbox1"> Attachment <input
			type="checkbox" id="checkbox1" class="checkbox style-2 pull-right">
			
		</label> </div>
		<div class="inner-addon left-addon">
		<i class="fa fa-dropbox"></i>
		<label for="checkbox2"> DropBox <input
			type="checkbox" id="checkbox2" class="checkbox style-3 pull-right">
		</label> </div>
		<div class="inner-addon left-addon">
		<i class="fa fa-file-o"></i>
		<label for="checkbox3"> Chatter File<input
			type="checkbox" id="checkbox3" class="checkbox style-3 pull-right">
		</label> </div>
		
		<div class="inner-addon left-addon">
		<i class="glyphicon glyphicon-film"></i>
		<label for="checkbox4"> Content <input
			type="checkbox" id="checkbox4" class="checkbox style-3 pull-right">
		</label>
		</div>
		
		<div class="inner-addon left-addon">
		<i class="fa fa-book "></i><label for="checkbox5"> Knowledge Article<input
			type="checkbox" id="checkbox5" class="checkbox style-3 pull-right">
		</label> 
		</div>
		
		<div class="inner-addon left-addon">
		<i class="fa fa-google"></i>
		<label for="checkbox6"> Google Drive <input
			type="checkbox" id="checkbox6" class="checkbox style-3 pull-right">
		</label>
		</div>
	
	</div>

</body>
</html>