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
    .when("/site", {
        templateUrl : "jsp/Sites.jsp",
        controller: 'SiteController'
    })
    .when("/search", {
        templateUrl : "jsp/search.jsp",
        
    });
})


.service('myService',function($http, $q){

    this.addItem = function (item) { 
       var response = $http({ 
             method: "post", 
             url: 'addObjects', 
             data: JSON.stringify(item),
             dataType: "json" 
     }); 
     return response;
    }
    
    this.uploadFileToUrl = function (file, uploadUrl) {
        //FormData, object of key/value pair for form fields and values
        var fileFormData = new FormData();
        fileFormData.append('file', file);

        var deffered = $q.defer();
        $http.post(uploadUrl, fileFormData, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}

        }).then(function (response) {
            deffered.resolve(response);

        });

        return deffered.promise;
    }

}).directive('demoFileModel', function ($parse) {
    return {
        restrict: 'A', //the directive can be used as an attribute only

        /*
         link is a function that defines functionality of directive
         scope: scope associated with the element
         element: element on which this directive used
         attrs: key value pair of element attributes
         */
        link: function (scope, element, attrs) {
            var model = $parse(attrs.demoFileModel),
                modelSetter = model.assign; //define a setter for demoFileModel

            //Bind change event on the element
            element.bind('change', function () {
                //Call apply on scope, it checks for value changes and reflect them on UI
                scope.$apply(function () {
                    //set the model value
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
});