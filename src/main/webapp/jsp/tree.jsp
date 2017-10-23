<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8" />
  <title>Tree</title>


 
</head>

<body ng-app="myApp">
	<br>


	<div class="container" ng-controller="TreeCtrl">
		<div class="row">
			<div class="col-md-6">
				<p>* All Salesfroce Objects *</p>
				<ul class="tree">
					<node-tree children="jsonData"></node-tree>
				</ul>

				<button class="btn btn-success" type="submit" value="Submit" id="button"
					ng-click="getSelected(item)">Save</button>

			</div>
			<div class="col-md-6">
				<div>
					<button ng-click="fieldRecords()" class="btn btn-success">Retrieve
						field data & send for indexing</button>
				</div>
				<br>

				<div>
					<h3 style="color: white">{{result}}</h3>
				</div>
			</div>


		</div>
	</div>

</body>

</html>

