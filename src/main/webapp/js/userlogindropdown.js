/**
 * 
 */
/*<script type="text/javascript">
        angular.module('drpdwnApp', []).controller('UserloginController', function ($scope, $http) {
            $scope.CompanyList = [];
            
            $scope.getCorporates =  function () {
            	$http({
            	    url: 'docsolrlogin', 
            	    method: "GET",
            	    isArray :  false
                }).success(function (result) {
                    $scope.CompanyList = result.d;
                });
            };
            //Calling the function to load the data on pageload
            $scope.getCorporates();
        });
    </script>*/


	var app = angular.module('MyApp', []);
	app.controller('UserloginController', function($scope, $http) {

      	$scope.getCorporateList = function(){
  
      	  $http({method: 'GET', url: 'populatePersonData'}).
	        success(function(data, status, headers, config) {
	        	$scope.CompanyList = data;	        	
	        }).
	        error(function(data, status, headers, config) {
	          // called asynchronously if an error occurs
	          // or server returns response with an error status.
	        });		    
	    };
	    $scope.getCorporateList();
});

