'use strict';

describe('Controller Tests', function() {

    describe('Child Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockChild, MockParent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockChild = jasmine.createSpy('MockChild');
            MockParent = jasmine.createSpy('MockParent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Child': MockChild,
                'Parent': MockParent
            };
            createController = function() {
                $injector.get('$controller')("ChildDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jh5App:childUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
