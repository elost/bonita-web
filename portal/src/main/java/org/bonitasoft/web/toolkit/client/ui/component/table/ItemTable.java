/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedJsId;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasCounters;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasDeploys;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasReaders;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.popup.DeleteMultipleItemsPopupAction;
import org.bonitasoft.web.toolkit.client.ui.action.popup.ItemDeletePopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Refreshable;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerDummy;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.DefaultItemTableCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class ItemTable extends AbstractTable implements Refreshable, FormNode {

    protected Table table = null;

    protected ItemDefinition itemDefinition = null;

    private final ArrayList<AbstractAttributeReader> columns = new ArrayList<AbstractAttributeReader>();

    protected List<ItemTableActionSet> actionSets = new LinkedList<ItemTableActionSet>();

    protected Map<String, ItemTableCellFormatter> cellFormatters = new HashMap<String, ItemTableCellFormatter>();

    private int actionColumnPosition = -1;

    private APIID defaultSelectedId = null;

    private Integer defaultSelectedLine = null;

    private boolean registerRefresh = true;

    private final HashMap<String, String> attributesForGroupedActions = new HashMap<String, String>();

    private boolean itemIdOnRow = false;

    protected final HashMap<String, IItem> loadedItems = new HashMap<String, IItem>();

    public ItemTable(final ItemDefinition itemDefinition) {
        this(null, itemDefinition);
    }

    public ItemTable(final ItemDefinition itemDefinition, final boolean itemIdOnRow) {
        this(null, itemDefinition);
        this.itemIdOnRow = itemIdOnRow;
    }

    public ItemTable(final JsId jsid, final ItemDefinition itemDefinition) {
        super(jsid);

        assert itemDefinition != null;
        this.itemDefinition = itemDefinition;
        table = new Table(jsid);
        setFillOnRefresh(true);
    }

    public HandlerRegistration addItemCheckedHandler(final ItemCheckedHandler handler) {
        return table.addItemCheckedHandler(handler);
    }

    public HandlerRegistration addItemUncheckedHandler(final ItemUncheckedHandler handler) {
        return table.addItemUncheckedHandler(handler);
    }

    public HandlerRegistration addItemTableLoadedHandler(final ItemTableLoadedHandler handler) {
        return addHandler(handler, ItemTableLoadedEvent.TYPE);
    }

    public ItemTable setRegisterRefresh(final boolean register) {
        registerRefresh = register;
        return this;
    }

    /**
     * Define the line to select by default.
     *
     * @param line
     *        the zero based index of the line
     */
    public ItemTable setDefaultSelectedLine(final Integer line) {
        defaultSelectedLine = line;
        return this;
    }

    public Integer getDefaultSelectedLine() {
        return defaultSelectedLine;
    }

    public void setDefaultSelectedId(final APIID id) {
        defaultSelectedId = id;
    }

    public APIID getDefaultSelectedId() {
        return defaultSelectedId;
    }

    public final ItemDefinition<?> getItemDefinition() {
        return itemDefinition;
    }

    @Override
    public final ItemTable setFillOnLoad(final boolean fillOnLoad) {
        super.setFillOnLoad(fillOnLoad);
        table.setFillOnLoad(fillOnLoad);
        return this;
    }

    public ItemTable setItems(final List<IItem> items) {
        resetLines();
        addItems(items);
        fireEvent(new ItemTableLoadedEvent(items));
        return this;
    }

    public final ItemTable addItems(final List<IItem> items) {

        if (!table.isSaveCheckboxes()) {
            table.clearSelectedIds();
        }

        int i = 1; // index of the first line of the table
        int selectedIndex = -1;
        for (final IItem item : items) {
            this.addItem(item);
            if (defaultSelectedId != null && defaultSelectedId.equals(item.getId())) {
                selectedIndex = i;
            } else if (defaultSelectedLine != null && defaultSelectedLine == i - 1) {
                selectedIndex = i;
            }
            i++;
        }
        table.updateHtml();
        if (selectedIndex > -1) {
            $(".tr_" + selectedIndex, getElement()).click();
        }

        return this;
    }

    protected final ItemTable addItem(final IItem item) {
        return this.addItem(item, null);
    }

    protected ItemTable addItem(final IItem item, final String className) {

        loadedItems.put(item.getId().toString(), item);

        if (actionColumnPosition == -1 && !actionSets.isEmpty()) {
            addActionColumn();
        }

        // Get default action (if there is one)
        Action defAction = null;
        final int lineNumber = table.getLinesNumber();
        if (defaultAction != null) {
            defAction = new Action() {

                @Override
                public void execute() {
                    defaultAction.addParameter("id", item.getId().toString());
                    defaultAction.addParameter("cell_index", String.valueOf(lineNumber + 1));// +1 because the line is build in the next
                                                                                             // instruction
                    defaultAction.execute();
                }
            };
        } else if (actionSets != null) {
            for (final ItemTableActionSet actionSet : actionSets) {
                defAction = actionSet.getDefaultAction(item);
                if (defAction != null) {
                    break;
                }
            }
        }
        if (defAction != null) {
            defAction.addParameter("id", item.getId().toString());
            defAction.addParameter("cell_index", String.valueOf(table.getLinesNumber() + 1));// +1 because the line is build in the next instruction
        }

        // Create the line component
        table.setItemIdOnRow(itemIdOnRow);
        table.addLine(item.getId().toString(), className, defAction, isGroupedActionAllowed(item));

        // Fill it with data columns
        addItemCells(item);

        // Add the actions column
        // this.addItemActions(item);

        return this;
    }

    private Boolean isGroupedActionAllowed(final IItem item) {
        if (attributesForGroupedActions.isEmpty()) {
            return true;
        }
        final Iterator it = attributesForGroupedActions.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (item.getAttributeValue(pairs.getKey().toString()).equals(pairs.getValue())) {
                return false;
            }
        }
        return true;
    }

    public ItemTable addAttributeToCheckForGroupedActions(final String attributeName, final String value) {
        attributesForGroupedActions.put(attributeName, value);
        return this;
    }

    protected final void addItemCells(final IItem item) {
        int index = 0;

        for (final AbstractAttributeReader column : columns) {

            if (index == actionColumnPosition) {
                this.addItemActions(item);
            } else {

                ItemTableCellFormatter cellFormatter = null;

                if (column instanceof HasDeploys) {
                    /**
                     * Get first cell formatter met for attributes deployed on our table
                     */
                    for (final String deployedAttribute : ((HasDeploys) column).getDeploys()) {

                        final String compoundAttribute = new DeployedJsId(deployedAttribute, column.getLeadAttribute()).toString();
                        if (cellFormatters.containsKey(compoundAttribute)) {
                            cellFormatter = cellFormatters.get(compoundAttribute);
                            break;
                        }
                    }

                } else {
                    cellFormatter = cellFormatters.get(column.getLeadAttribute());
                }

                if (cellFormatter == null) {
                    cellFormatter = new DefaultItemTableCellFormatter();
                }

                cellFormatter.setTable(table);
                cellFormatter.setColumn(table.getLastColumn());
                cellFormatter.setLine(table.getLastLine());
                cellFormatter.setAttribute(column);
                cellFormatter.setItem(item);

                cellFormatter.execute();
            }
            index++;
        }
    }

    protected void addItemActions(final IItem item) {
        this.addItemActions(item, getActionsFor(item));
    }

    protected void addItemActions(final IItem item, final List<ItemTableAction> actions) {
        final ContainerDummy<ItemTableAction> actionComponents = new ContainerDummy<ItemTableAction>();
        for (final ItemTableAction itemTableAction : actions) {
            itemTableAction.addParameter("cell_index", String.valueOf(table.getLinesNumber()));
            itemTableAction.addParameter("id", item.getId().toString());
            actionComponents.append(itemTableAction);
        }
        table.addCell(actionComponents);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemTable resetFilters() {
        table.resetFilters();
        return this;
    }

    public final ItemTable addTextFilter(final String label, final String tooltip, final String name) {
        table.addTextFilter(label, tooltip, name);
        return this;
    }

    public final ItemTable addTextFilter(final String label, final String tooltip, final String name, final String defaultValue) {
        table.addTextFilter(label, tooltip, name, defaultValue);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Map<String, String> values) {
        table.addSelectFilter(label, tooltip, name, values);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final LinkedHashMap<String, String> values,
            final String defaultValue) {
        table.addSelectFilter(label, tooltip, name, values, defaultValue);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler) {
        table.addSelectFilter(label, tooltip, name, filler);
        return this;
    }

    public final ItemTable addSelectFilter(final String label, final String tooltip, final String name, final Filler<?> filler, final String defaultValue) {
        table.addSelectFilter(label, tooltip, name, filler, defaultValue);
        return this;
    }

    public final ItemTable addHiddenFilter(final String name, final String value) {
        table.addHiddenFilter(name, value);
        return this;
    }

    public final ItemTable addHiddenFilter(final String name, final APIID value) {
        if (value == null) {
            return addHiddenFilter(name, (String) null);
        }
        return addHiddenFilter(name, value.toString());
    }

    public ItemTable resetHiddenFilters() {
        table.resetHiddenFilters();
        return this;
    }

    public final ItemTable addHiddenFilters(final Map<String, String> filters) {
        table.addHiddenFilters(filters);
        return this;
    }

    public final ItemTable addHiddenFilter(final Map<String, String> filters) {
        table.addHiddenFilters(filters);
        return this;
    }

    public final Map<String, String> getFilters() {
        return table.getFilters();
    }

    public final Map<String, String> getHiddenFilters() {
        return table.getHiddenFilters();
    }

    public final List<String> getDeploys() {
        final List<String> result = new ArrayList<String>();
        for (final AbstractAttributeReader reader : columns) {
            getAttributeReaderDeploys(reader, result);
        }
        return result;
    }

    private List<String> getAttributeReaderDeploys(final AbstractAttributeReader reader, final List<String> deploys) {
        if (reader instanceof HasDeploys) {
            deploys.addAll(((HasDeploys) reader).getDeploys());
        }
        if (reader instanceof HasReaders) {
            final Map<String, AbstractAttributeReader> readers = ((HasReaders) reader).getAttributeReaders();
            if (readers != null) {
                for (final AbstractAttributeReader attributeReader : readers.values()) {
                    getAttributeReaderDeploys(attributeReader, deploys);
                }
            }
        }
        return deploys;
    }

    public final List<String> getCounters() {
        final List<String> result = new ArrayList<String>();
        // go through readers
        for (final AbstractAttributeReader reader : columns) {
            if (reader instanceof HasCounters) {
                result.addAll(((HasCounters) reader).getCounters());
            }
        }

        // go through actions
        for (final ItemTableActionSet<?> actionSet : actionSets) {
            if (actionSet instanceof HasCounters) {
                result.addAll(((HasCounters) actionSet).getCounters());
            }
        }

        return result;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COLUMNS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ItemTable addActionColumn() {
        // this.table.addColumn(new JsId("actions"), _("Actions"));
        actionColumnPosition = columns.size();
        columns.add(null);

        return this;
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label) {
        return this.addColumn(attribute, label, false);
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label, final boolean sorted, final boolean ascendant) {
        table.addColumn(new JsId(attribute.getLeadAttribute()), label, attribute.getLeadAttribute(), sorted, ascendant);
        columns.add(attribute);

        return this;
    }

    public final ItemTable addColumn(final AbstractAttributeReader attribute, final String label, final boolean sortable) {
        JsId columnJsid;

        // Get first attribute's reader first deploy to identify column. Used by formatters.
        if (attribute instanceof HasDeploys) {
            columnJsid = new DeployedJsId(((HasDeploys) attribute).getDeploys().get(0), attribute.getLeadAttribute());
        } else {
            columnJsid = new JsId(attribute.getLeadAttribute());
        }

        table.addColumn(columnJsid, label, sortable ? attribute.getLeadAttribute() : null);
        columns.add(attribute);

        return this;
    }

    public final ItemTable addColumn(final String attributeName, final String label) {
        return this.addColumn(attributeName, label, false);
    }

    public final ItemTable addColumn(final String attributeName, final String label, final boolean sorted, final boolean ascendant) {
        table.addColumn(new JsId(attributeName), label, attributeName, sorted, ascendant);
        columns.add(new AttributeReader(attributeName));

        return this;
    }

    public final ItemTable addColumn(final String attributeName, final String label, final boolean sortable) {
        if (sortable) {
            table.addColumn(new JsId(attributeName), label, attributeName);
        } else {
            table.addColumn(new JsId(attributeName), label);
        }
        columns.add(new AttributeReader(attributeName));

        return this;
    }

    /**
     * @param jsid
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getColumn(org.bonitasoft.web.toolkit.client.ui.JsId)
     */
    public TableColumn getColumn(final JsId jsid) {
        return table.getColumn(jsid);
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getColumns()
     */
    public List<TableColumn> getColumns() {
        return table.getColumns();
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getLastColumn()
     */
    public TableColumn getLastColumn() {
        return table.getLastColumn();
    }

    public final ArrayList<String> getColumnsName() {
        final ArrayList<String> results = new ArrayList<String>();
        for (final AbstractAttributeReader attribute : columns) {
            results.add(attribute.getLeadAttribute());
        }
        return results;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CELL FORMATTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final ItemTable addCellFormatter(final String attributeName, final ItemTableCellFormatter cellFormatter) {
        cellFormatters.put(attributeName, cellFormatter);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Action defaultAction = null;

    /**
     * @param defaultAction
     *        the defaultAction to set
     */
    public final ItemTable setDefaultAction(final Action defaultAction) {
        this.defaultAction = defaultAction;
        return this;
    }

    public final ItemTable resetActions() {
        actionSets.clear();
        return this;
    }

    public final ItemTable setActions(final ItemTableActionSet itemTableActionSet) {
        resetActions();
        return addActions(itemTableActionSet);
    }

    public final ItemTable addActions(final ItemTableActionSet itemTableActionSet) {
        itemTableActionSet.setItemTable(this);
        actionSets.add(itemTableActionSet);

        return this;
    }

    public final ItemTable addGroupedAction(final JsId id, final String label, final String tooltip, final Action action) {
        table.addGroupedAction(id, label, tooltip, action);
        return this;
    }

    /**
     * Add an action to the table - action not link to checkbox, always visible
     */
    public final ItemTable addAction(final Link link) {
        table.addAction(link);
        return this;
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#getGroupedActions()
     */
    public List<Link> getGroupedActions() {
        return table.getGroupedActions();
    }

    /**
     * @param link
     * @param force
     *        Force the visibility of the action (can't be disabled)
     * @return
     */
    public final ItemTable addGroupedAction(final Link link, final boolean force) {
        table.addGroupedAction(link, force);
        return this;
    }

    public final ItemTable addGroupedAction(final Link link) {
        return addGroupedAction(link, false);
    }

    /**
     * @param label
     * @param tooltip
     * @param action
     * @param force
     *        Force the visibility of the action (can't be disabled)
     */
    public final ItemTable addGroupedAction(final JsId id, final String label, final String tooltip, final Action action, final boolean force) {
        table.addGroupedAction(id, label, tooltip, action, force);
        return this;
    }

    public final ItemTable addGroupedDeleteAction(final String tooltip, final ItemDefinition definition) {
        table.addGroupedAction(new JsId("delete"), _("Delete"), tooltip, new ItemDeletePopupAction(definition));
        return this;
    }

    public final ItemTable addGroupedMultipleDeleteAction(final String tooltip, final ItemDefinition definition, final String itemName,
            final String itemNamePlural) {
        table.addGroupedAction(new JsId("delete"), _("Delete"), tooltip, new DeleteMultipleItemsPopupAction(definition, itemName, itemNamePlural));
        return this;
    }

    public List<ItemTableAction> getActionsFor(final IItem item) {
        if (actionSets == null) {
            return new LinkedList<ItemTableAction>();
        }

        final List<ItemTableAction> actions = new LinkedList<ItemTableAction>();

        for (final ItemTableActionSet set : actionSets) {
            actions.addAll(set.getActionsFor(item));
        }
        return actions;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public final ItemTable setNbLinesByPage(final int nbLinesByPage) {
        table.setPager(0, 0, nbLinesByPage);
        return this;
    }

    public final ItemTable setView(final VIEW_TYPE view) {
        table.setView(view);
        return this;
    }

    public final ItemTable saveCheckboxes(final boolean save) {
        table.saveCheckboxes(save);
        return this;
    }

    public final ItemTable setRefreshEvery(final int milliseconds) {
        table.setRefreshEvery(milliseconds);
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HTML GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {
        if (actionSets != null && actionSets.size() > 0 && actionColumnPosition == -1) {
            final JsId actionJsId = new JsId("actions");

            table.addColumn(actionJsId, _("Actions"));
            if (actionColumnPosition > -1) {
                table.setColumnPos(actionJsId, actionColumnPosition);
            }
        }

        return table.getElement();
    }

    @Override
    protected void postProcessHtml() {
        super.postProcessHtml();

        final ItemTableFiller filler = new ItemTableFiller();
        table.setRefreshFiller(filler);
        filler.setTarget(this);
        setFiller(filler);

    }

    @Override
    public void refresh() {
        refresh(null);
    }

    public void refresh(final Action callback) {
        runFillers(callback);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC DELEGATIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final ItemTable addClass(final String className) {
        if (table != null) {
            table.addClass(className);
        }
        return this;
    }

    public final int getNbPages() {
        return table.getNbPages();
    }

    public final int getNbLinesByPage() {
        return table.getNbLinesByPage();
    }

    public final int getPage() {
        return table.getPage();
    }

    public final List<String> getSelectedIds() {
        return table.getSelectedIds();
    }

    public final String getSearch() {
        return table.getSearch();
    }

    /**
     * @see org.bonitasoft.web.toolkit.client.ui.component.table.Table#setSearch(java.lang.String)
     */
    public final ItemTable setSearch(final String query) {
        table.setSearch(query);
        return this;
    }

    public final ItemTable setSelectLineOnClick(final boolean value) {
        table.setSelectLineOnClick(value);
        return this;
    }

    public final String getOrder() {
        return table.getOrder();
    }

    public final ItemTable resetLines() {
        table.resetLines();
        loadedItems.clear();
        return this;
    }

    public final ItemTable setPager(final int currentPage, final int nbPages, final int nbLinesByPage) {
        table.setPager(currentPage, nbPages, nbLinesByPage);
        return this;
    }

    public final ItemTable setPage(final int page) {
        table.setPage(page);
        return this;
    }

    public final void changePage(final int page) {
        table.changePage(page);
    }

    @Override
    public final String toString() {
        return table.toString();
    }

    public final boolean hasGroupedActions() {
        return table.hasGroupedActions();
    }

    public final ItemTable setOrder(final String sortName, final boolean sortAscending) {
        table.setOrder(sortName, sortAscending);
        return this;
    }

    public ItemTable setShowSearch(final boolean show) {
        table.setShowSearch(show);
        return this;
    }

    public void updateView() {
        table.updateView();
    }

    public IItem getItem(final String itemId) {
        return loadedItems.get(itemId);
    }

}
