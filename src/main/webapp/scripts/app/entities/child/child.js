'use strict';

angular.module('jh5App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('child', {
                parent: 'entity',
                url: '/childs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jh5App.child.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/child/childs.html',
                        controller: 'ChildController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('child');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('child.detail', {
                parent: 'entity',
                url: '/child/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jh5App.child.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/child/child-detail.html',
                        controller: 'ChildDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('child');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Child', function($stateParams, Child) {
                        return Child.get({id : $stateParams.id});
                    }]
                }
            })
            .state('child.new', {
                parent: 'child',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/child/child-dialog.html',
                        controller: 'ChildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('child', null, { reload: true });
                    }, function() {
                        $state.go('child');
                    })
                }]
            })
            .state('child.edit', {
                parent: 'child',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/child/child-dialog.html',
                        controller: 'ChildDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Child', function(Child) {
                                return Child.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('child', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('child.delete', {
                parent: 'child',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/child/child-delete-dialog.html',
                        controller: 'ChildDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Child', function(Child) {
                                return Child.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('child', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
