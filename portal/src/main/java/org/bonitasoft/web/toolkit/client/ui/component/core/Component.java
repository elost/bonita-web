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
package org.bonitasoft.web.toolkit.client.ui.component.core;

import static com.google.gwt.query.client.GQuery.$;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.user.client.Element;

/**
 * Define the default structure of all VisualElements of USerXP toolkit
 *
 * @author Séverin Moussel
 */
public abstract class Component extends AbstractComponent {

    /**
     * List of HTML classes to set on the root DOM element.
     */
    private final Set<String> classes = new TreeSet<String>();

    /**
     * Id used to help JS and CSS to locate the element.<br>
     * This ID will be used as class and/or class suffix.
     */
    private JsId jsId = null;

    /**
     * Dom Element use to store the Component.
     */
    protected Element element = null;

    private boolean enabled = true;

    /**
     * Default constructor
     */
    public Component() {
    }

    /**
     * /**
     * Default constructor
     *
     * @param jsid
     *        The JsiD to set. It will be use to help JS and CSS to locate the element.<br>
     *        This ID will be used as class and/or class suffix.
     */
    public Component(final JsId jsid) {
        jsId = jsid;
        if (jsid != null) {
            addClass(jsid.toString());
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JS OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The options to pass to javascript using the attribute REL and filling it with a JSon array.
     */
    private final Map<String, Object> jsOptions = new HashMap<String, Object>();

    /**
     * Get the value of a currently set JsOption
     *
     * @param name
     *        The name of the option to get
     * @return This method returns the value of the option.
     */
    public String getJsOption(final String name) {
        return jsOptions.get(name).toString();
    }

    /**
     * Add a new jsOption : {@link Component#jsOptions}
     *
     * @param name
     *        The name of the option to add.
     * @param value
     *        The value of the option to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public Component addJsOption(final String name, final Object value) {
        jsOptions.put(name, value);
        return this;
    }

    /**
     * Add a new boolean jsOption : {@link Component#jsOptions}
     *
     * @param name
     *        The name of the option to add.
     * @param value
     *        The value of the option to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public Component addJsOption(final String name, final boolean value) {
        jsOptions.put(name, new Boolean(value));
        return this;
    }

    /**
     * Add a new int jsOption : {@link Component#jsOptions}
     *
     * @param name
     *        The name of the option to add.
     * @param value
     *        The value of the option to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public Component addJsOption(final String name, final int value) {
        jsOptions.put(name, new Integer(value));
        return this;
    }

    /**
     * Add a new long jsOption : {@link Component#jsOptions}
     *
     * @param name
     *        The name of the option to add.
     * @param value
     *        The value of the option to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public Component addJsOption(final String name, final long value) {
        jsOptions.put(name, new Long(value));
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS / GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The id to set on the root DOM element.
     */
    protected String id = null;

    /**
     * Get the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the the id to set on the root DOM element.
     *
     * @param id
     *        the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Id used to help JS and CSS to locate the element.<br>
     * This ID will be used as class and/or class suffix.
     */
    @Override
    public final JsId getJsId() {
        return jsId;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HTML GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Construct the HTML tree using a set of com.google.gwt.user.client.Element.
     *
     * @return This function returns the root Element of this VisualElement.
     */
    @Override
    protected final List<Element> _getElements() {
        final LinkedList<Element> res = new LinkedList<Element>();
        res.add(getElement());
        return res;
    }

    /**
     * Construct the HTML tree using a set of com.google.gwt.user.client.Element.
     *
     * @return This function returns the root Element of this VisualElement.
     */
    protected abstract Element makeElement();

    @Override
    public final Element getElement() {
        if (!generated) {

            preProcessHtml();

            element = makeElement();

            // Apply class
            for (final String className : classes) {
                element.addClassName(className);
            }

            setElementState(enabled);

            // Apply ID if not already set
            if (id != null) {
                element.setId(id);
            }

            if (tooltip != null) {
                element.setAttribute("title", tooltip);
            }

            // Set JsOptions
            if (jsOptions.size() > 0) {
                element.setAttribute("rel", JSonSerializer.serialize(jsOptions));
            }

            postProcessHtml();
            generated = true;
        }
        return element;
    }

    @Override
    public void resetGeneration() {
        generated = false;
        $(element).remove();
        element = null;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected final boolean isInDom(final Element e) {
        return super.isInDom(e);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());

        if (isGenerated()) {
            sb.append(" > ");
            sb.append(getElement().getNodeName());
            if (getElement().getId() != null) {
                sb.append("#");
                sb.append(getElement().getId());
            }
            if (getElement().getClassName() != null) {
                sb.append(".");
                sb.append(getElement().getClassName().replaceAll(" ", "."));
            }
        } else if (jsId != null) {
            sb.append(" > #");
            sb.append(jsId.toString());
        }
        return sb.toString();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CLASS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Component addClass(final String className) {
        if (className != null) {
            final String cName = className.trim();
            if (cName.length() > 0) {
                final String[] classes = cName.split(" ");
                for (int i = 0; i < classes.length; i++) {
                    this.classes.add(classes[i]);
                    if (isGenerated()) {
                        element.addClassName(classes[i]);
                    }
                }
            }
        }
        return this;
    }

    public final AbstractComponent removeClass(final String className) {
        final String[] classes = className.split(" ");
        for (int i = 0; i < classes.length; i++) {
            this.classes.remove(classes[i]);
            if (isGenerated()) {
                element.removeClassName(classes[i]);
            }
        }
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (element != null) {
            setElementState(enabled);
        }
    }

    private void setElementState(final boolean enabled) {
        if (!enabled) {
            element.addClassName("disabled");
        } else {
            element.removeClassName("disabled");
        }
    }

    public boolean hasClass(final String cssClass) {
        if (isGenerated()) {
            return element.getClassName().contains(" " + cssClass) || element.getClassName().contains(cssClass + " ");
        }
        return classes.contains(cssClass);
    }
}
