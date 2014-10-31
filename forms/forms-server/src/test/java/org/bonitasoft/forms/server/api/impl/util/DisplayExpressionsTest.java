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
package org.bonitasoft.forms.server.api.impl.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormWidget;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 */
public class DisplayExpressionsTest {

    /**
     * Enable assertions for this test
     */
    static {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
    }

    @Test
    public void testWeCanRetrieveDisplayExpressionOfAWidget() {
        final Expression displayExp1 = new Expression();
        final Expression displayExp2 = new Expression();
        final List<FormWidget> widgets = Arrays.asList(
                aWidget("widget1", displayExp1),
                aWidget("widget2", displayExp2));

        final Map<WidgetExpressionEntry, Expression> map =
                new DisplayExpressions(widgets).asMap();

        assertEquals(2, map.size());
        assertEquals(displayExp1,
                map.get(new WidgetExpressionEntry("widget1", ExpressionId.WIDGET_DISPLAY_CONDITION)));
        assertEquals(displayExp2,
                map.get(new WidgetExpressionEntry("widget2", ExpressionId.WIDGET_DISPLAY_CONDITION)));
    }

    @Test
    public void testAWidgetWithoutDisplayExpressionIsNotListed() {
        final Expression displayExp = new Expression();
        final List<FormWidget> widgets = Arrays.asList(
                aWidget("widget1", null),
                aWidget("widget2", displayExp));

        final Map<WidgetExpressionEntry, Expression> map =
                new DisplayExpressions(widgets).asMap();

        assertEquals(1, map.size());
        assertEquals(displayExp,
                map.get(new WidgetExpressionEntry("widget2", ExpressionId.WIDGET_DISPLAY_CONDITION)));
    }

    @Test
    public void testWeCanRetrieveDisplayExpressionsOfNestedGroups() {
        final Expression displayExp1 = new Expression();
        final Expression displayExp2 = new Expression();
        final Expression displayExp3 = new Expression();
        final List<FormWidget> widgets = Arrays.asList(
                aGroup("group1", displayExp1,
                        Arrays.asList(
                                aGroup("group2", displayExp2,
                                        Arrays.asList(
                                                aWidget("child", displayExp3))))));

        final Map<WidgetExpressionEntry, Expression> map =
                new DisplayExpressions(widgets).asMap();

        assertEquals(3, map.size());
        assertEquals(displayExp1,
                map.get(new WidgetExpressionEntry("group1", ExpressionId.WIDGET_DISPLAY_CONDITION)));
        assertEquals(displayExp2,
                map.get(new WidgetExpressionEntry("group2", ExpressionId.WIDGET_DISPLAY_CONDITION)));
        assertEquals(displayExp3,
                map.get(new WidgetExpressionEntry("child", ExpressionId.WIDGET_DISPLAY_CONDITION)));
    }

    @Test
    public void testWeCanRetrieveChildExpressionOfAGroupWihtoutExpression() {
        final Expression displayExp = new Expression();
        final List<FormWidget> widgets = Arrays.asList(
                aGroup("group", null,
                        Arrays.asList(
                                aWidget("child", displayExp))));

        final Map<WidgetExpressionEntry, Expression> map =
                new DisplayExpressions(widgets).asMap();

        assertEquals(1, map.size());
        assertEquals(displayExp,
                map.get(new WidgetExpressionEntry("child", ExpressionId.WIDGET_DISPLAY_CONDITION)));
    }

    private FormWidget aWidget(final String id, final Expression displayExp) {
        final FormWidget widget = new FormWidget();
        widget.setId(id);
        widget.setDisplayConditionExpression(displayExp);
        return widget;
    }

    private FormWidget aGroup(final String id, final Expression displayExp, final List<FormWidget> children) {
        final FormWidget group = new FormWidget();
        group.setId(id);
        group.setDisplayConditionExpression(displayExp);
        group.setChildWidgets(children);
        return group;
    }
}
