var myApp = angular.module('enterprise', ['angularBootstrapNavTree']);

	myApp.controller('SocialController', function($window, $scope) {
		$scope.shareOnFacebook = function() {
			$window.location.replace("/docsolrlogin/social/facebook/signin");
		};
		
			  
	});


	myApp.controller('TreeCtrl', function($window,$scope,$http) {
		
		 $scope.name = 'Docsolr';

		
		 $scope.getZipFile = function() {
			 $http.get("recieveZip").then(function(response) {
				 $scope.jsonData = response.data;
			 });
		 }
		 $scope.getZipFile();

	
	});
	
	

	
	
	