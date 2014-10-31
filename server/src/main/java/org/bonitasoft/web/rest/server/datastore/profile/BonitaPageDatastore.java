/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Fabio Lombardi
 */
public class BonitaPageDatastore {

    public BonitaPageDatastore() {
    }

    public List<IItem> fromBonitaPageItemToIItem() {
        final List<IItem> pageListDest = new ArrayList<IItem>();
        final List<BonitaPageItem> pageListSource = new ArrayList<BonitaPageItem>(new PageLister().getPages());
        for (final BonitaPageItem page : pageListSource) {
            pageListDest.add(page);
        }
        return pageListDest;
    }

    public ItemSearchResult<BonitaPageItem> search(final int page, final int resultsByPage, final String search, final Map<String, String> filters,
            final String orders) {
        final List<BonitaPageItem> pages = new PageLister().getPages();
        return new ItemSearchResult<BonitaPageItem>(page, resultsByPage, pages.size(), new ArrayList<BonitaPageItem>(pages));
    }

    /**
     * @param id
     * @return
     */
    public BonitaPageItem get(final APIID id) {
        return new PageLister().getPage(id.toString());
    }

}
