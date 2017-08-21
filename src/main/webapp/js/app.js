var myApp = angular.module('enterprise',[ 'tree.directives']);

	myApp.controller('SocialController', function($window, $scope) {
		$scope.shareOnFacebook = function() {
			$window.location.replace("/docsolrlogin/social/facebook/signin");
		};
		
			  
	});


	myApp.controller('TreeCtrl', function($window,$scope,$http,myService) {
		
		 $scope.name = 'Docsolr';	
		 $scope.getZipFile = function() {
			 $http.get("recieveZip").then(function(response) {
				 $scope.jsonData = response.data;
			 });
		 }
		 $scope.getZipFile();
		 
		 $scope.save = function(){
			    $scope.albumNameArray = [];
			    angular.forEach($scope.jsonData, function(getZipFile){
			      if (getZipFile.selected) $scope.albumNameArray.push(getZipFile.name);
			    });
			  }
	
	
	//	 $scope.selectedItems = [];
	 
		 $scope.getSelected = function(item){
			 $scope.selectedItems = [];
			 $scope.selectedItemsObject = [];
			 
			 function checkChildren(c,mykey) {
	          angular.forEach(c.children, function (c,key) {
	             if (c.checked){
	                $scope.selectedItems.push({"selected":c.name});
	                $scope.selectedItemsObject.push({"key":$scope.jsonData[mykey].name,"selected":c.name});
	             }
	              checkChildren(c,key);
	          });
	     }
	     
	     angular.forEach($scope.jsonData, function(value, key) {
	    	 
	          if (value.checked){
	            $scope.selectedItems.push({"selected":value.name});
	            $scope.selectedItemsObject.push({"key":$scope.jsonData[key].name,"selected":value.name});
	          }
	           checkChildren(value,key);
	      });
	     
	     myService.addItem($scope.selectedItemsObject)
	   };
	   
		
	 
	});

	 myApp.service('myService',function($http){

		    this.addItem = function (item) { 
		       var response = $http({ 
		             method: "post", 
		             url: 'addObjects', 
		             data: JSON.stringify(item),
		             dataType: "json" 
		     }); 
		     return response;
		}

		});

	 
	

	
	