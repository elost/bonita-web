/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.profile.entry;

import static org.junit.Assert.assertEquals;

import org.bonitasoft.engine.profile.ProfileEntrySearchDescriptor;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.server.datastore.profile.ProfileSearchDescriptorConverter;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 */
public class ProfileEntrySearchDescriptorConverterTest {

    @Test
    public void testWeCanConvertTheProfileEntryId() {
        final String descriptor = new ProfileEntrySearchDescriptorConverter()
                .convert(ProfileEntryItem.ATTRIBUTE_ID);

        assertEquals(ProfileEntrySearchDescriptor.ID, descriptor);
    }

    @Test
    public void testWeCanConvertTheProfileEntryIndex() {
        final String descriptor = new ProfileEntrySearchDescriptorConverter()
                .convert(ProfileEntryItem.ATTRIBUTE_INDEX);

        assertEquals(ProfileEntrySearchDescriptor.INDEX, descriptor);
    }

    @Test
    public void testWeCanConvertTheProfileEntryName() {
        final String descriptor = new ProfileEntrySearchDescriptorConverter()
                .convert(ProfileEntryItem.ATTRIBUTE_NAME);

        assertEquals(ProfileEntrySearchDescriptor.NAME, descriptor);
    }

    @Test
    public void testWeCanConvertTheProfileEntryProfileId() {
        final String descriptor = new ProfileEntrySearchDescriptorConverter()
                .convert(ProfileEntryItem.ATTRIBUTE_PARENT_ID);

        assertEquals(ProfileEntrySearchDescriptor.PARENT_ID, descriptor);
    }

    @Test
    public void testWeCanConvertTheProfileEntryParentId() {
        final String descriptor = new ProfileEntrySearchDescriptorConverter()
                .convert(ProfileEntryItem.ATTRIBUTE_PROFILE_ID);

        assertEquals(ProfileEntrySearchDescriptor.PROFILE_ID, descriptor);
    }

    @Test(expected = RuntimeException.class)
    public void testTryingToConvertUnexistingDescriptorThrowAnException() {
        new ProfileSearchDescriptorConverter().convert("someUnsupportedAttribute");
    }

}
