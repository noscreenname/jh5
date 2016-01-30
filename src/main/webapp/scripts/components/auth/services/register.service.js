'use strict';

angular.module('jh5App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


