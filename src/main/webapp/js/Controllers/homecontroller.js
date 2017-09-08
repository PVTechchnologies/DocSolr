'use strict';
angular.module('myApp')
  .controller('HomeCtrl', ['$scope', function ($scope) {
	  if(document.getElementById('username') != null)
		  $scope.username = document.getElementById('username').value ;
  }]);