<!DOCTYPE html>
<html ng-app="enterprise">

<head>
  <meta charset="utf-8" />
  <title>Tree</title>
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" />  
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.5/angular.min.js"></script>
  <script src="https://code.angularjs.org/1.3.0-beta.5/angular.js"></script>
    <link rel="stylesheet" href="css/tree.css"/>
  <script src="js/app.js"></script>
  <script src="js/treedirective.js"></script>

 
</head>

<body ng-controller="TreeCtrl">
 <p>Hello {{name}}!</p>
	
	
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
   
</body>

</html>

