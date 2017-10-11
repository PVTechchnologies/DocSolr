<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Docsolr</title>

</head>
<body>
	<div class="container">
		<div calss="row">
			<div class="col-md-6">
				<div ng-repeat="records in apiRecordData">
					<br> <br>
					<div ng-repeat="record in records track by $index">

						<table>
							<tr ng-repeat="child in record.children track by $index">
								<td>{{child.name}}</td>
								<td>{{child.value}}</td>
							</tr>
						</table>
					</div>

				</div>
				</div>
<br> <br>
				<div class="col-md-6">
					<button type=button>
						<a href="#!solr">Post Salesforce data to Solr</a>
					</button>
				</div>

			</div>
			
</body>
</html>