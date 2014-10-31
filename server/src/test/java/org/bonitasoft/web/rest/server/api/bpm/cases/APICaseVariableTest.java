/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bpm.cases;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseVariableDatastore;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 */
public class APICaseVariableTest extends APITestWithMock {

    private APICaseVariable apiCaseVariable;

    @Mock
    private CaseVariableDatastore datastore;

    @Before
    public void initializeMocks() {
        initMocks(this);
        apiCaseVariable = spy(new APICaseVariable());

        doReturn(datastore).when(apiCaseVariable).getDefaultDatastore();
    }

    private APIID buildAPIID(final long caseId, final String variableName) {
        final APIID makeAPIID = makeAPIID(asList(String.valueOf(caseId), variableName));
        makeAPIID.setItemDefinition(CaseVariableDefinition.get());
        return makeAPIID;
    }

    private Map<String, String> buildUpdateParameters(final String newValue, final String className) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put(CaseVariableItem.ATTRIBUTE_VALUE, newValue);
        map.put(CaseVariableItem.ATTRIBUTE_TYPE, className);
        return map;
    }

    private Map<String, String> buildCaseIdFilter(final long caseId) {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseVariableItem.ATTRIBUTE_CASE_ID, String.valueOf(caseId));
        return filters;
    }

    @Test
    public void updateUpdateTheVariableValue() {
        final long caseId = 1L;
        final String variableName = "aName";
        final String newValue = "newValue";
        final Map<String, String> parameters = buildUpdateParameters(newValue, String.class.getName());
        final APIID apiid = buildAPIID(caseId, variableName);

        apiCaseVariable.runUpdate(apiid, parameters);

        verify(datastore).updateVariableValue(caseId, variableName, String.class.getName(), newValue);
    }

    @Test
    public void weCheckAttributesBeforeUpdating() {
        final APICaseVariableAttributeChecker attributeChecker = mock(APICaseVariableAttributeChecker.class);
        apiCaseVariable.setAttributeChecker(attributeChecker);
        final APIID apiid = buildAPIID(1L, "aName");
        final Map<String, String> attributes = buildUpdateParameters("newValue", String.class.getName());

        apiCaseVariable.runUpdate(apiid, attributes);

        verify(attributeChecker).checkUpdateAttributes(attributes);
    }

    @Test
    public void weCheckFiltersBeforeSearching() {
        final APICaseVariableAttributeChecker attributeChecker = mock(APICaseVariableAttributeChecker.class);
        apiCaseVariable.setAttributeChecker(attributeChecker);
        final Map<String, String> filters = buildCaseIdFilter(1L);

        apiCaseVariable.runSearch(0, 10, null, null, filters, null, null);

        verify(attributeChecker).checkSearchFilters(filters);
    }

    @Test
    public void searchIsFilteredByCaseId() {
        final long expectedCaseId = 1L;
        final Map<String, String> filters = buildCaseIdFilter(expectedCaseId);

        apiCaseVariable.runSearch(0, 10, null, null, filters, null, null);

        verify(datastore).findByCaseId(expectedCaseId, 0, 10);
    }

    @Test
    public void getSearchInDatastoreById() {
        final long expectedCaseId = 1L;
        final String expectedVariableName = "aName";
        final APIID apiid = buildAPIID(expectedCaseId, expectedVariableName);

        apiCaseVariable.runGet(apiid, null, null);

        verify(datastore).findById(expectedCaseId, expectedVariableName);
    }
}
