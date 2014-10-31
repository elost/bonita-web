package org.bonitasoft.web.rest.server.datastore.profile.member;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestGroupFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfile;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfileFactory;
import org.bonitasoft.test.toolkit.organization.profiles.TestProfileMember;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.api.profile.APIProfileMember;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ProfileMemberDatastoreIntegrationTest extends AbstractConsoleTest {

    private APIProfileMember apiProfileMember;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiProfileMember = new APIProfileMember();
        apiProfileMember.setCaller(getAPICaller(getInitiator().getSession(), "API/portal/profileMember"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Ignore("TestProfileFactory need to be updated to stop using command")
    @Test
    public void testWeCanSearchAUser() {
        final TestProfile profile = TestProfileFactory.newProfile(getInitiator().getSession()).create();
        final TestProfileMember membership = profile.addMember(getInitiator());
        final Map<String, String> filters = createSearchFilters(profile, ProfileMemberItem.VALUE_MEMBER_TYPE_USER);

        final ItemSearchResult<ProfileMemberItem> search = apiProfileMember.search(0, 1, null, null, filters);

        Assert.assertEquals(APIID.makeAPIID(membership.getMembershipId()), search.getResults().get(0).getId());
    }

    /*
     * FIXME engine throw npe when trying to add group to a profile
     */
    @Ignore
    @Test
    public void testWeCanSearchAGroup() {
        final TestProfile profile = TestProfileFactory.newProfile(getInitiator().getSession()).create();
        final TestGroup group = TestGroupFactory.createGroup("name", "desciption");
        final TestProfileMember membership = profile.addMember(group);
        final Map<String, String> filters = createSearchFilters(profile, ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP);

        final ItemSearchResult<ProfileMemberItem> search = apiProfileMember.search(0, 1, null, null, filters);

        Assert.assertEquals(APIID.makeAPIID(membership.getMembershipId()), search.getResults().get(0).getId());
    }

    private Map<String, String> createSearchFilters(final TestProfile profile, final String memberType) {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, String.valueOf(profile.getId()));
        filters.put(ProfileMemberItem.FILTER_MEMBER_TYPE, memberType);
        return filters;
    }

}
