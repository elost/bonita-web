package org.bonitasoft.console.client.angular;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AngularResourceRootTest {

    @Test
    public void should_add_slash_to_url_as_context() {
        final AngularResourceRoot root = new AngularResourceRoot();

        assertEquals("../portal.js/path/to/resource", root.contextualize("path/to/resource"));
    }
}
