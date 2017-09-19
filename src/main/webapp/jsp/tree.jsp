<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8" />
  <title>Tree</title>


 
</head>

<body  ng-app="myApp">
<br>
 
	
	<div class="container">
	<div calss="row">
	<div class="col-md-6">
	<p> * All Salesfroce Objects *</p>
    <ul class="tree">
        <node-tree children="jsonData"></node-tree>
    </ul>
    
    <button  type="submit"  value = "Submit" id="button" class="btn btn-primary btn-md  login-button" ng-click="getSelected(item)">Save</button>
    
    <br/>
    <br/>
    Selected: {{selectedItemsObject}}
    <ul>
      <li ng-repeat="item in selectedItemsObject">
      </li>
    </ul>
   </div>
   <div class="col-md-6">
   <button type=button><a href="#!field" >Retrieve Field Data</a></button>
   </div>
   
   </div>
</body>

</html>

