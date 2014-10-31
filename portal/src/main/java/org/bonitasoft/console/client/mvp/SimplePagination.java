/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.mvp;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.event.ActionEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SimplePagination extends Composite {

    private int page = 0, pageSize = 0, total = 0;

    private final Paginate paginate;

    interface Binder extends UiBinder<HTMLPanel, SimplePagination> {

    }

    private static Binder binder = GWT.create(Binder.class);

    @UiField
    SpanElement label;

    @UiField
    ButtonAction previous;

    @UiField
    ButtonAction next;

    @UiConstructor
    public SimplePagination(final int page, final int pageSize, final int total, final Paginate paginate) {
        initWidget(binder.createAndBindUi(this));
        this.paginate = paginate;
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        previous.setVisible(pageSize < total);
        next.setVisible(pageSize < total);
        buildWidget();
    }

    private void buildWidget() {
        final int startIndex = page * pageSize + 1;
        int endIndex = pageSize + startIndex - 1;
        if (endIndex > total) {
            endIndex = total;
        }
        label.setInnerText(stringify(startIndex, endIndex, total));
        enable(previous, page > 0);
        enable(next, endIndex < total);
    }

    private String stringify(final int startIndex, final int endIndex, final int total) {
        if (startIndex == endIndex) {
            return _("%start_result% of %total_results%",
                    new Arg("start_result", startIndex),
                    new Arg("total_results", total));
        }
        return _("%start_result% - %end_result% of %total_results%",
                new Arg("start_result", startIndex),
                new Arg("end_result", endIndex),
                new Arg("total_results", total));
    }

    private void enable(final Component component, final boolean enabled) {
        component.setEnabled(enabled);
        component.setStyleName("disable", !enabled);
    }

    public void setPage(final int page) {
        this.page = page;
        buildWidget();
    }

    @UiHandler("previous")
    void doPrevious(final ActionEvent e) {
        paginate.loadPage(page - 1, pageSize);
    }

    @UiHandler("next")
    void doNext(final ActionEvent e) {
        paginate.loadPage(page + 1, pageSize);
    }
}
