'use strict';

angular.module('jh5App')
	.controller('ParentDeleteController', function($scope, $uibModalInstance, entity, Parent) {

        $scope.parent = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Parent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
