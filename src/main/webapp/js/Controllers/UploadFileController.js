'use strict';

angular.module('myApp')
  .controller('UploadFileController', ['$scope','myService', function ($scope,myService) {
	  $scope.uploadFile = function () {
          var file = $scope.myFile;
          var uploadUrl = "uploadFile", //Url of webservice/api/server
              promise = myService.uploadFileToUrl(file, uploadUrl);

          promise.then(function (response) {
              $scope.serverResponse = response.data;
          }, function () {
              $scope.serverResponse = 'An error has occurred';
          })
      };
  }]);