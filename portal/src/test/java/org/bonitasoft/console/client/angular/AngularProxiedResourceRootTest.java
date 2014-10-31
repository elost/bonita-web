package org.bonitasoft.console.client.angular;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AngularProxiedResourceRootTest {

    @Test
    public void should_add_proxy_address_to_url_as_context() {
        final AngularProxiedResourceRoot root = new AngularProxiedResourceRoot();

        assertEquals("http://127.0.0.1:9000/path/to/resource", root.contextualize("path/to/resource"));
    }
}
