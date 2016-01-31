'use strict';

angular.module('jh5App')
	.controller('ChildDeleteController', function($scope, $uibModalInstance, entity, Child) {

        $scope.child = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Child.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
