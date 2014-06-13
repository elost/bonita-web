'use strict';

describe('Service: users', function () {

  // load the service's module
  beforeEach(module('portaljsApp'));

  // instantiate service
  var users;
  beforeEach(inject(function (_User_) {
    users = _User_;
  }));

  it('should do something', function () {
    expect(!!users).toBe(true);
  });

});
