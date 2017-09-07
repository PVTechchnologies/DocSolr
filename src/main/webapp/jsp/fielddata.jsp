<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Profile Page | Beingjavaguys.com</title>

</head>
<body >
	
	 <table>
      <tr ng-repeat="record in apiRecordData">
      	<td>
      	<div ng-repeat="children in record.children track by $index">
      	<table border="1">
      	<tr><td>{{children.name}}</td><td>{{children.value}}</td></tr>
      	</br>
      	</table>
      	</div>
      	</td>
      </tr>
    </table>	
</body>
</html>