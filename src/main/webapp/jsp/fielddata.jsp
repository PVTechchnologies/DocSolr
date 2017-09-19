<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>User Profile Page | Beingjavaguys.com</title>

</head>
<body >
<div ng-repeat="records in apiRecordData">
<br>
<br>
<div ng-repeat="record in records track by $index">
	<!-- <div class="row" ng-repeat="child in record.children track by $index">
		<div class="col-sm-3">{{child.name}}</div>
		<div class="col-sm-3">{{child.value}}</div>
	</div> -->
	<table>
		<tr ng-repeat="child in record.children track by $index">
			<td>{{child.name}}</td>
			<td>{{child.value}}</td>
		</tr>
	</table>
</div>
<br>
<br>
	
</body>
</html>