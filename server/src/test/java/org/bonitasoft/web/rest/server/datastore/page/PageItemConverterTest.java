/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.page;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.bonitasoft.engine.page.Page;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.BonitaRestAPIFactory;
import org.bonitasoft.web.rest.server.framework.RestAPIFactory;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PageItemConverterTest extends APITestWithMock{

    @BeforeClass
    public static void initEnvironnement() {
        RestAPIFactory.setDefaultFactory(new BonitaRestAPIFactory());
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
    }

    @Test
    public void should_convert_engineItem_Page_to_portalItem_PageItem(){

        //Given
        final PageItemConverter pageitemConverter = new PageItemConverter();
        final Page engineItem = mock(Page.class);
        when(engineItem.getId()).thenReturn(1l);
        when(engineItem.getName()).thenReturn("page1");
        when(engineItem.getDisplayName()).thenReturn("Page 1");
        when(engineItem.isProvided()).thenReturn(false);
        when(engineItem.getDescription()).thenReturn("This is a page description");
        when(engineItem.getInstalledBy()).thenReturn(1l);
        when(engineItem.getInstallationDate()).thenReturn(new Date(1));
        when(engineItem.getLastModificationDate()).thenReturn(new Date(1));
        when(engineItem.getLastUpdatedBy()).thenReturn(1l);
        when(engineItem.getContentName()).thenReturn("page1.zip");

        //When
        final PageItem pageItem = pageitemConverter.convert(engineItem);

        //Assert
        assertTrue(pageItem.getId().equals(engineItem.getId()));
        assertTrue(pageItem.getUrlToken().equals(engineItem.getName()));
        assertTrue(pageItem.getDisplayName().equals(engineItem.getDisplayName()));
        assertTrue(pageItem.isProvided() == engineItem.isProvided());
        assertTrue(pageItem.getDescription().equals(engineItem.getDescription()));
        assertTrue(pageItem.getCreatedByUserId().equals(engineItem.getInstalledBy()));
        assertTrue(pageItem.getCreationDate().equals(engineItem.getInstallationDate()));
        assertTrue(pageItem.getLastUpdateDate().equals(engineItem.getLastModificationDate()));
        assertTrue(pageItem.getUpdatedByUserId().equals(engineItem.getLastUpdatedBy()));
        assertTrue(pageItem.getContentName().equals(engineItem.getContentName()));

    }
}
