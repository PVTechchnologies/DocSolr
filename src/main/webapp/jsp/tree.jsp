<!DOCTYPE html>
<html ng-app="enterprise">

<head>
  <meta charset="utf-8" />
  <title>Tree</title>
  <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" />
 
  <script>
    document.write('<base href="' + document.location + '" />');
  </script>
  <script src="https://code.angularjs.org/1.3.0-beta.5/angular.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
  <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
  <script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.11.0.min.js"></script>
  <script src="js/app.js"></script>
 
 
</head>

	<body ng-controller="TreeCtrl">
	 <p>Hello {{name}}!</p>
	   <p>Metadata from Salesforce: {{jsonData}}</p>
	</body>

</html>
