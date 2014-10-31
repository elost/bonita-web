package org.bonitasoft.web.rest.server;

import static org.junit.Assert.assertTrue;

import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition;
import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoUser;
import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoValue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 */
public class BonitaRestAPIFactoryTest extends APITestWithMock {

    BonitaRestAPIFactory factory;

    @Before
    public void setUp() {
        factory = new BonitaRestAPIFactory();
    }

    @Test
    public void should_provide_an_APICustomUserInfoDefinition_when_requesting_custom_user_info_definition() {
        assertTrue(factory.defineApis("customuserinfo", "definition") instanceof APICustomUserInfoDefinition);
    }

    @Test
    public void should_provide_an_APICustomUserInfoUser_when_requesting_custom_user_info() {
        assertTrue(factory.defineApis("customuserinfo", "user") instanceof APICustomUserInfoUser);
    }

    @Test
    public void should_provide_an_APICustomUserInfoValue_when_requesting_custom_user_info_value() {
        assertTrue(factory.defineApis("customuserinfo", "value") instanceof APICustomUserInfoValue);
    }
}
