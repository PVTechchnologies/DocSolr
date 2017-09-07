'use strict';

angular.module('myApp').controller('FieldCtrl', ['$window','$scope','$http','myService', function ($window,$scope,$http,myService) {

		 $scope.name = 'Docsolr';	
		 
		 $scope.getRecords = function() {
			 $http.get("recieveRecord").then(function(response) {
				 $scope.apiRecordData = response.data;
			 });
		 }
		 $scope.getRecords();
		 
		
  }]);