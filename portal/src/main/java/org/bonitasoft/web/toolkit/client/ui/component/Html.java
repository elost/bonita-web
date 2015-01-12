package org.bonitasoft.web.toolkit.client.ui.component;

import java.util.LinkedList;

import org.bonitasoft.web.toolkit.client.ui.component.core.Components;
import org.bonitasoft.web.toolkit.client.ui.html.XML;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;

public class Html extends Components {

    private final LinkedList<Element> elements;

    public Html(final String html) {
        elements = XML.makeElements(html);
    }

    public Html(final SafeHtml html) {
        this(html.asString());
    }

    public Html(final com.google.gwt.dom.client.Element element) {
        elements = new LinkedList<Element>();
        elements.add((Element) element);
    }

    @Override
    protected LinkedList<Element> makeElements() {
        return elements;
    }

}
