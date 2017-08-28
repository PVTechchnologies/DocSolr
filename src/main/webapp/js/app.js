/*var myApp = angular.module('enterprise',[ 'tree.directives']);*/



angular.module('myApp').controller('SocialController', function($window, $scope) {
		$scope.shareOnFacebook = function() {
			$window.location.replace("/docsolrlogin/social/facebook/signin");
		};
		
			  
	});




	 
	

	
	