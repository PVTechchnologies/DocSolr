var app = angular.module("myApp", ["ngRoute", 'tree.directives']);
angular.module('myApp')
.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "jsp/home.jsp"
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