
app.controller('SolrCtrl', ['$window','$scope','$http','myService', function ($window,$scope,$http,myService) {

		 $scope.result='';	
		 $scope.solrdata='';
		 $scope.inSearch="";
		 $scope.CompanyList;
		 
	
		 
		 $scope.solrRespnseData = function() {
			 console.log("asdlasfjsaflas")
			 	$http({
            	    url: 'SolrSearch', 
            	    method: "POST",
            	    data : {"name": $scope.inSearch},
            	    isArray :  false
                }).then(function (result) {
                    $scope.CompanyList = result.data;
                });
		 }
		
  }]);