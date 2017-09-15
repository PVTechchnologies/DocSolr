'use strict';

angular.module('myApp')
  .controller('TreeCtrl', ['$window','$scope','$http','myService', function ($window,$scope,$http,myService) {

		 $scope.name = 'Docsolr';	
		 $scope.getZipFile = function() {
			 $http.get("recieveObjects").then(function(response) {
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


//		 $scope.selectedItems = [];
	 
		 $scope.getSelected = function(item){
			 $scope.selectedItems = [];
			 $scope.selectedItemsObject = [];
			 
			 
			 function checkChildren(c,mykey) {
	          angular.forEach(c.children, function (c,key) {
	             if (c.checked){
	                $scope.selectedItems.push({"selected":c.name});
	                $scope.selectedItemsObject.push({"key":$scope.jsonData[mykey].name,"selected":c.name,"idvalue":c.id});
	                
	             }
	              checkChildren(c,key);
	          });
	     }
	     
	     angular.forEach($scope.jsonData, function(value, key) {
	    	 
	          if (value.checked){
	            $scope.selectedItems.push({"selected":value.name});
	            $scope.selectedItemsObject.push({"key":$scope.jsonData[key].name,"selected":value.name,"idvalue":value.id});
	            
	          }
	           checkChildren(value,key);
	      });
	     
	     myService.addItem($scope.selectedItemsObject)
	   };

  }]);