'use strict';

angular.module('jh5App').controller('ParentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Parent',
        function($scope, $stateParams, $uibModalInstance, entity, Parent) {

        $scope.parent = entity;
        $scope.load = function(id) {
            Parent.get({id : id}, function(result) {
                $scope.parent = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jh5App:parentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.parent.id != null) {
                Parent.update($scope.parent, onSaveSuccess, onSaveError);
            } else {
                Parent.save($scope.parent, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
