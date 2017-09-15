var app = angular.module("myApp", ["ngRoute", 'tree.directives']);
angular.module('myApp')
.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "jsp/dochome.jsp",
        controller: 'HomeCtrl'
    })
    .when("/login", {
        templateUrl : "jsp/login/docsolrlogin.jsp"
        	
    })
     .when("/signup", {
        templateUrl : "jsp/login/registration.jsp"
        	
    })
    .when("/userview", {
        templateUrl : "jsp/user.jsp"
        	
    })
    .when("/treeview", {
        templateUrl : "jsp/tree.jsp",
        controller: 'TreeCtrl'
    })
    .when("/field", {
        templateUrl : "jsp/fielddata.jsp",
        controller: 'FieldCtrl'
    })
    .when("/search", {
        templateUrl : "jsp/search.jsp",
        
    });
})


.service('myService',function($http){

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