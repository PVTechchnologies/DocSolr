<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8" />
  <title>Tree</title>


 
</head>

<body  ng-app="myApp">
 <p>Hello {{name}}!</p>
	
	
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
   
   <a href="#!field">Field Data</a>
</body>

</html>

