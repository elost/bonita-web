'use strict';

angular.module('portaljsApp')
  .factory('User', [ '$resource', function ($resource) {
    return $resource('../API/identity/user/:id', {id:'@id'});
  }]);
