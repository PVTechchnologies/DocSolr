
app.controller('SolrCtrl', ['$window','$scope','$http','myService', function ($window,$scope,$http,myService) {

		 $scope.result='';	
		 $scope.solrdata='';
		 $scope.inSearch="";
		 $scope.CompanyList;
		 
		 $scope.solrRespnseData = function() {
			 $scope.attachmentcount=0;
			 	$http({
            	    url: 'SolrSearch', 
            	    method: "POST",
            	    data : {"name": $scope.inSearch},
            	    isArray :  false
                }).then(function (result) {
                    $scope.CompanyList = result.data;
                    angular.forEach($scope.CompanyList, function(value, key){
                        if(value.type === 'ContentVersion')
                        	$scope.attachmentcount= $scope.attachmentcount+1;
                     });
                });
		 }
		 
		 
		 var searchelement = document.getElementById("searchinput");
		 searchelement.addEventListener("keydown", function (e) {
		     if (e.keyCode === 13) {  //checks whether the pressed key is "Enter"
		    	 $scope.solrRespnseData();
		     }
		 });
		
  }]);

