package org.bonitasoft.console.common.server.sso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalSSOManagerTest {

    protected InternalSSOManager internalSSOManager;

    @Before
    public void setUp() {
        internalSSOManager = InternalSSOManager.getInstance();
    }

    @After
    public void tearDown() {
        internalSSOManager = null;
    }

    @Test
    public void testAdd() {
        assertNotNull("Cannot add ", internalSSOManager.add(new Object()));
    }

    @Test
    public void testGet() {
        final Object o = new Object();
        final String token = internalSSOManager.add(o);
        assertEquals("Cannot get ", internalSSOManager.get(token), o);
    }

    @Test
    public void testRemove() {
        final Object o = new Object();
        final String token = internalSSOManager.add(o);
        internalSSOManager.remove(token);
        assertNull("Cannot remove ", internalSSOManager.get(token));
    }

}
