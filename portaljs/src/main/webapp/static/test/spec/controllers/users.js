'use strict';

describe('Controller: UsersCtrl', function () {

	// load the controller's module
	beforeEach(module('portaljsApp'));

	var UsersCtrl,
		scope, $usersResources, resultingUsers;

	// Initialize the controller and a mock scope
	beforeEach(inject(function ($controller, $rootScope) {
		scope = $rootScope.$new();
		resultingUsers = [];
		$usersResources = {
				query : function(){
				}
			};
		spyOn($usersResources, 'query').andReturn(resultingUsers);
		UsersCtrl = $controller('UsersCtrl', {
			$scope: scope,
			User: $usersResources
		});
	}));

	it('should attach a list empty users to the scope', function () {
		expect(scope.users.length).toBe(0);
	});
});
