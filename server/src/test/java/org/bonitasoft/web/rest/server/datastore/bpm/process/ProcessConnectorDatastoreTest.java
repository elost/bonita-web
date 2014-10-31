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
package org.bonitasoft.web.rest.server.datastore.bpm.process;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_PROCESS_ID;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.connector.ConnectorCriterion;
import org.bonitasoft.engine.bpm.connector.ConnectorImplementationDescriptor;
import org.bonitasoft.engine.bpm.connector.ConnectorNotFoundException;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 */
@SuppressWarnings("unchecked")
public class ProcessConnectorDatastoreTest extends APITestWithMock {

    @Mock
    private ProcessAPI processAPI;

    private ProcessConnectorDatastore processConnectorDatastore;

    @Before
    public void initializeMocks() {
        initMocks(this);

        processConnectorDatastore = spy(new ProcessConnectorDatastore(null));

        doReturn(processAPI).when(processConnectorDatastore).getProcessAPI();
    }

    private APIID anAPIID(final String id, final String name, final String version) {
        final APIID apiid = APIID.makeAPIID(id, name, version);
        apiid.setItemDefinition(ProcessConnectorDefinition.get());
        return apiid;
    }

    private ConnectorImplementationDescriptor aConnectorImplementationDescriptor(final String name) {
        final ConnectorImplementationDescriptor descriptor = new ConnectorImplementationDescriptor("implementationClassName",
                name, "version", "definitionId", "definitionVersion", EMPTY_LIST);
        return descriptor;
    }

    private HashMap<String, String> aProcessIdFilter(final String processId) {
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ATTRIBUTE_PROCESS_ID, processId);
        return filters;
    }

    private ProcessConnectorItem convertToItem(final ConnectorImplementationDescriptor descriptor1, final String processId) {
        final ProcessConnectorItem item = processConnectorDatastore.convertEngineToConsoleItem(descriptor1);
        item.setProcessId(processId);
        return item;
    }

    @Test
    public void getRetrieveConnectorImplementationAndSetProcessId() throws Exception {
        final ConnectorImplementationDescriptor descriptor = aConnectorImplementationDescriptor("aName");
        when(processAPI.getConnectorImplementation(1L, "name", "1")).thenReturn(descriptor);

        final ProcessConnectorItem fetchedItem = processConnectorDatastore.get(anAPIID("1", "name", "1"));

        final ProcessConnectorItem expectedItem = convertToItem(descriptor, "1");
        assertTrue(areEquals(expectedItem, fetchedItem));
    }

    @Test(expected = APIException.class)
    public void getThrowExceptionIfProcessDefinitionIsNotFound() throws Exception {
        when(processAPI.getConnectorImplementation(anyLong(), anyString(), anyString()))
                .thenThrow(new ConnectorNotFoundException(null));

        processConnectorDatastore.get(anAPIID("1", "name", "1"));
    }

    @Test(expected = APIException.class)
    public void searchThrowExceptionIfProcessDefinitionIsNotFound() {
        when(processAPI.getConnectorImplementations(anyLong(), anyInt(), anyInt(), any(ConnectorCriterion.class)))
                .thenThrow(new NullPointerException());

        processConnectorDatastore.search(0, 10, null, null, aProcessIdFilter("1"));
    }

    @Test
    public void searchReturnAllProcessConnectorForAProcessDefinitionId() {
        final ConnectorImplementationDescriptor descriptor1 = aConnectorImplementationDescriptor("aName");
        final ConnectorImplementationDescriptor descriptor2 = aConnectorImplementationDescriptor("anOtherName");
        when(processAPI.getConnectorImplementations(anyLong(), anyInt(), anyInt(), any(ConnectorCriterion.class)))
                .thenReturn(asList(descriptor1, descriptor2));

        final ItemSearchResult<ProcessConnectorItem> search =
                processConnectorDatastore.search(0, 10, null, "DEFINITION_ID_ASC", aProcessIdFilter("1"));

        final ProcessConnectorItem expectedItem1 = convertToItem(descriptor1, "1");
        final ProcessConnectorItem expectedItem2 = convertToItem(descriptor2, "1");
        assertTrue(areEquals(search.getResults().get(0), expectedItem1));
        assertTrue(areEquals(search.getResults().get(1), expectedItem2));
    }

    @Test
    public void testConvertEngineToConsoleItem() {
        final ConnectorImplementationDescriptor descriptor = new ConnectorImplementationDescriptor("implementationClassName",
                "name", "version", "definitionId", "definitionVersion", EMPTY_LIST);
        final ProcessConnectorItem expectedItem = new ProcessConnectorItem();
        expectedItem.setName("definitionId");
        expectedItem.setVersion("definitionVersion");
        expectedItem.setImplementationName("name");
        expectedItem.setImplementationVersion("version");
        expectedItem.setClassname("implementationClassName");

        final ProcessConnectorItem convertedItem = processConnectorDatastore.convertEngineToConsoleItem(descriptor);

        assertTrue(areEquals(expectedItem, convertedItem));
    }

}
