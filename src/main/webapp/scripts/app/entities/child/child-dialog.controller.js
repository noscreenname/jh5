'use strict';

angular.module('jh5App').controller('ChildDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Child', 'Parent',
        function($scope, $stateParams, $uibModalInstance, entity, Child, Parent) {

        $scope.child = entity;
        $scope.parents = Parent.query();
        $scope.load = function(id) {
            Child.get({id : id}, function(result) {
                $scope.child = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jh5App:childUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.child.id != null) {
                Child.update($scope.child, onSaveSuccess, onSaveError);
            } else {
                Child.save($scope.child, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
