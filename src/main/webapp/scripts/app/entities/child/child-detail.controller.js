'use strict';

angular.module('jh5App')
    .controller('ChildDetailController', function ($scope, $rootScope, $stateParams, entity, Child, Parent) {
        $scope.child = entity;
        $scope.load = function (id) {
            Child.get({id: id}, function(result) {
                $scope.child = result;
            });
        };
        var unsubscribe = $rootScope.$on('jh5App:childUpdate', function(event, result) {
            $scope.child = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
