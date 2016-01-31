'use strict';

angular.module('jh5App')
    .controller('ParentDetailController', function ($scope, $rootScope, $stateParams, entity, Parent) {
        $scope.parent = entity;
        $scope.load = function (id) {
            Parent.get({id: id}, function(result) {
                $scope.parent = result;
            });
        };
        var unsubscribe = $rootScope.$on('jh5App:parentUpdate', function(event, result) {
            $scope.parent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
