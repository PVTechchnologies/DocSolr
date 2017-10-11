


app.controller('SiteController', function ($scope, $http) {
            
	$scope.siteList;
	$scope.siteLibraryMap;
	$scope.siteFolderMap;
	$scope.siteFileMap;
	
            $scope.getSharepointdata =  function () {
            	/*$http({
            	    url: "Sites", 
            	    method: "GET"
                }).success(function(data, status, headers, config) {
    	        	console.log(data);        	
    	        }).
    	        error(function(data, status, headers, config) {
    	          // called asynchronously if an error occurs
    	          // or server returns response with an error status.
    	        });	*/
            	
            	 
            	 $http.get("SiteUrl").then(function(response) {
    				 console.log(response);
    				 $scope.siteObject=response.data;
    				 $scope.siteList = response.data.siteList;
    					$scope.siteLibraryMap = response.data.siteLibraryMap;
    					$scope.siteFolderMap = response.data.siteFolderMap;
    					$scope.siteFileMap = response.data.siteFileMap;
    			 });
            };
            //Calling the function to load the data on pageload
            $scope.getSharepointdata();
        });
