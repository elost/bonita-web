'use strict';

angular.module('portaljsApp')
  .controller('UsersCtrl', [ '$scope', 'User', function ($scope, User) {
	$scope.users = User.query({'p':0,'c':10,'o':'lastname ASC', 'f' :'enabled=true'});
	$scope.gridOptions = { data : 'users', columnDefs: [{field:'lastname', displayName:'Last Name'}, {field:'firstname', displayName:'First Name'}]};
	
}]);
