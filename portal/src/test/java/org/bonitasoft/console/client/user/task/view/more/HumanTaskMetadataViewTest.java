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
package org.bonitasoft.console.client.user.task.view.more;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT.DISPLAY;
import static org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT.DISPLAY_RELATIVE;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bonitasoft.console.client.uib.formatter.Formatter;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMetadataView.Binder;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.ClientDateFormater;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.google.gwt.i18n.client.impl.LocaleInfoImpl;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeUiBinderProvider;

/**
 * @author Julien Reboul
 */
@RunWith(GwtMockitoTestRunner.class)
public class HumanTaskMetadataViewTest {

    // @GwtMock
    // SpanElement spanElement;
    //
    // @GwtMock
    // SpanElement spanElement2;

    // @GwtMock
    // AnchorElement anchorElement;
    //
    // @GwtMock
    // DivElement divElement;
    //
    // @GwtMock
    // LabelElement labelElement;
    //
    // @GwtMock
    // ParagraphElement paragraphElement;
    //
    @GwtMock
    LocaleInfoImpl localeInfoImpl;

    @BeforeClass
    public static void classSetUp() {
        I18n.getInstance();
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        CommonDateFormater.setDateFormater(new ClientDateFormater());
    }

    @Before
    public void setUp() {
        final DateTimeFormatInfo dateTimeFormatInfo = mock(DateTimeFormatInfo.class);
        when(localeInfoImpl.getDateTimeFormatInfo()).thenReturn(dateTimeFormatInfo);
        when(dateTimeFormatInfo.ampms()).thenReturn(new String[] { "AM", "PM" });
        GwtMockito.useProviderForType(Binder.class, new FakeUiBinderProvider());
    }

    @Test
    public void testHumanTaskMetadataViewShouldOnlyHaveAssignedUserAndLastUpdate() {
        final IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_ABORTED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        when(humanTaskItem.getAssignedDate()).thenReturn("2011-01-04 12:25:00.123");
        final UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);

        final HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        final String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(humanTaskMetadataView.priority, times(1)).setInnerText(priorityValue);
        final String assignedToValue = Formatter.formatUser(userItem);
        verify(humanTaskMetadataView.assignedTo, times(1)).setInnerText(assignedToValue);
        verify(humanTaskMetadataView.doneBy, times(1)).setInnerText(assignedToValue);
        final String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(humanTaskMetadataView.dueDate, times(1)).setInnerText(dueDateValue);
        verify(humanTaskMetadataView.doneByContainer, never()).removeFromParent();

        final String lastUpdateDateLabel = humanTaskMetadataView.messages.last_update_date_label() + ": ";
        final String lastUpdateDateTitle = humanTaskMetadataView.messages.last_update_date_title();
        verify(humanTaskMetadataView.labelDoneOn, times(1)).setTitle(lastUpdateDateTitle);
        verify(humanTaskMetadataView.labelDoneOn, times(1)).setInnerText(lastUpdateDateLabel);
        final String assignedDate = Formatter.formatDate(humanTaskItem.getAssignedDate(), DISPLAY);
        verify(humanTaskMetadataView.assignedDate, times(1)).setInnerText(assignedDate);

    }

    @Test
    public void testHumanTaskMetadataViewShouldHaveAssignedUserAndSubstituteAndLastUpdate() {
        final IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_FAILED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(2L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        when(humanTaskItem.getAssignedDate()).thenReturn("2011-01-04 12:25:00.123");
        final UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);
        final UserItem userSubstituteItem = mock(UserItem.class);
        when(userSubstituteItem.getFirstName()).thenReturn("PMFirstname");
        when(userSubstituteItem.getLastName()).thenReturn("PMLastname");
        when(humanTaskItem.getExecutedBySubstituteUser()).thenReturn(userSubstituteItem);

        final HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        final String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(humanTaskMetadataView.priority, times(1)).setInnerText(priorityValue);
        final String assignedToValue = Formatter.formatUser(userItem);
        verify(humanTaskMetadataView.assignedTo, times(1)).setInnerText(assignedToValue);
        final String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(humanTaskMetadataView.dueDate, times(1)).setInnerText(dueDateValue);
        final String executedByUser = Formatter.formatUser(humanTaskItem.getExecutedBySubstituteUser()) + _(" for ")
                + Formatter.formatUser(humanTaskItem.getExecutedByUser());
        verify(humanTaskMetadataView.doneBy, times(1)).setInnerText(executedByUser);
        verify(humanTaskMetadataView.doneByContainer, never()).removeFromParent();

        final String lastUpdateDateLabel = humanTaskMetadataView.messages.last_update_date_label() + ": ";
        final String lastUpdateDateTitle = humanTaskMetadataView.messages.last_update_date_title();
        verify(humanTaskMetadataView.labelDoneOn, times(1)).setTitle(lastUpdateDateTitle);
        verify(humanTaskMetadataView.labelDoneOn, times(1)).setInnerText(lastUpdateDateLabel);
        final String assignedDate = Formatter.formatDate(humanTaskItem.getAssignedDate(), DISPLAY);
        verify(humanTaskMetadataView.assignedDate, times(1)).setInnerText(assignedDate);

    }

    @Test
    public void testHumanTaskMetadataViewShouldHaveAssignedUserAndSubstituteAndDoneOn() {
        final IHumanTaskItem humanTaskItem = mock(IHumanTaskItem.class);
        when(humanTaskItem.getPriority()).thenReturn(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        when(humanTaskItem.getState()).thenReturn(IHumanTaskItem.VALUE_STATE_COMPLETED);
        when(humanTaskItem.getExecutedByUserId()).thenReturn(APIID.makeAPIID(1L));
        when(humanTaskItem.getExecutedBySubstituteUserId()).thenReturn(APIID.makeAPIID(2L));
        when(humanTaskItem.getCaseId()).thenReturn(APIID.makeAPIID(10L));
        when(humanTaskItem.getDueDate()).thenReturn("2012-12-05 00:00:00.000");
        final String assignedDateStr = "2011-01-04 12:25:00.123";
        when(humanTaskItem.getAssignedDate()).thenReturn(assignedDateStr);
        final UserItem userItem = mock(UserItem.class);
        when(userItem.getFirstName()).thenReturn("UserFirstname");
        when(userItem.getLastName()).thenReturn("UserLastname");
        when(humanTaskItem.getExecutedByUser()).thenReturn(userItem);
        when(humanTaskItem.getAssignedUser()).thenReturn(userItem);
        final UserItem userSubstituteItem = mock(UserItem.class);
        when(userSubstituteItem.getFirstName()).thenReturn("PMFirstname");
        when(userSubstituteItem.getLastName()).thenReturn("PMLastname");
        when(humanTaskItem.getExecutedBySubstituteUser()).thenReturn(userSubstituteItem);

        final HumanTaskMetadataView humanTaskMetadataView = new HumanTaskMetadataView(humanTaskItem);
        final String priorityValue = Formatter.formatPriority(IHumanTaskItem.VALUE_PRIORITY_NORMAL);
        verify(humanTaskMetadataView.priority, times(1)).setInnerText(priorityValue);
        final String assignedToValue = Formatter.formatUser(userItem);
        verify(humanTaskMetadataView.assignedTo, times(1)).setInnerText(assignedToValue);
        final String dueDateValue = Formatter.formatDate(humanTaskItem.getDueDate(), DISPLAY_RELATIVE);
        verify(humanTaskMetadataView.dueDate, times(1)).setInnerText(dueDateValue);
        final String executedByUser = Formatter.formatUser(humanTaskItem.getExecutedBySubstituteUser()) + _(" for ")
                + Formatter.formatUser(humanTaskItem.getExecutedByUser());
        verify(humanTaskMetadataView.doneBy, times(1)).setInnerText(executedByUser);
        verify(humanTaskMetadataView.doneByContainer, never()).removeFromParent();

        final String lastUpdateDateLabel = humanTaskMetadataView.messages.done_on_label() + ": ";
        verify(humanTaskMetadataView.doneBy, never()).setTitle(anyString());
        verify(humanTaskMetadataView.labelDoneOn, times(1)).setInnerText(lastUpdateDateLabel);
        final String assignedDate = Formatter.formatDate(assignedDateStr, DISPLAY);
        verify(humanTaskMetadataView.assignedDate, times(1)).setInnerText(assignedDate);

    }

}
