
app.controller('SolrCtrl', ['$window','$scope','$http','myService', function ($window,$scope,$http,myService) {

		 $scope.result='';	
		 $scope.solrdata='';
		 $scope.inSearch="";
		 $scope.CompanyList;
		 
		 $scope.postXml = function() {
			 $http.get("sendrecord").then(function(response) {
				 $scope.apiRecordData = response.data;
				 if( $scope.apiRecordData==200)
					 {
					 	$scope.result = "Data Posted Successfully" ;
					 }
				 else{
					 $scope.result = "Error while posting Data" 
				 }
			 });
		 }
		
		 
		 
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
		 $scope.postXml();
  }]);