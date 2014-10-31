package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.ext.guice.SelfInjectingServerResourceModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;

@RunWith(MockitoJUnitRunner.class)
public class TimerEventTriggerResourceIT {

    private final ProcessAPI processAPI = mock(ProcessAPI.class);

    class TestModule extends AbstractModule {

        @Provides
        ProcessAPI getPAPI() {
            return processAPI;
        }

        @Override
        protected void configure() {
        }

    }

    private volatile Client client;

    private volatile Component component;

    @Before
    public void setUp() throws Exception {

        Guice.createInjector(new TestModule(), new SelfInjectingServerResourceModule());

        client = new Client(Protocol.HTTP);

        if (component == null) {
            component = new Component();
            component.getServers().add(Protocol.HTTP, 9173);
            component.getDefaultHost().attachDefault(new BonitaRestletApplication());
        }

        if (!component.isStarted()) {
            component.start();
        }

    }

    @After
    public void tearDown() throws Exception {
        client.stop();
        component.stop();
        component = null;
    }

    @Test
    public void searchTimerEventTriggerInstancesShouldReturnEmptyListAsJson() throws SearchException {
        @SuppressWarnings("rawtypes")
        final SearchResult searchResult = mock(SearchResult.class);
        doReturn(Collections.emptyList()).when(searchResult).getResult();

        doReturn(searchResult).when(processAPI).searchTimerEventTriggerInstances(eq(1L), any(SearchOptions.class));
        final Client client2 = new Client(Protocol.HTTP);
        final Request request = new Request(Method.GET, "http://localhost:9173/bpm/timerEventTrigger?caseId=1&p=0&c=10&s=");
        final Response response = client2.handle(request);

        assertThat(response.getStatus().getCode()).isEqualTo(200);
        assertThat(response.getEntityAsText()).isEqualTo("[]");
    }

    //    @Test(expected = TimerEventTriggerInstanceNotFoundException.class)
    //    public void updateTimerEventTriggersShouldThrowExceptionIfTimerNotFound() throws Exception {
    //        doReturn("1").when(resource).getAttribute(TimerEventTriggerResource.ID_PARAM_NAME);
    //
    //        final TimerEventTrigger trigger = new TimerEventTrigger(System.currentTimeMillis());
    //        resource.updateTimerEventTrigger(trigger);
    //    }
    //
    //    @Test
    //    public void updateTimerEventTriggersShouldReturnStatusCode200() throws Exception {
    //        final long timerEventTriggerId = 1L;
    //        doReturn("" + timerEventTriggerId).when(resource).getAttribute(TimerEventTriggerResource.ID_PARAM_NAME);
    //        final ProcessAPI processAPI = mock(ProcessAPI.class);
    //        doReturn(processAPI).when(resource).getEngineProcessAPI();
    //        final Date date = new Date();
    //        doReturn(date).when(processAPI).updateExecutionDateOfTimerEventTriggerInstance(eq(timerEventTriggerId), any(Date.class));
    //        final TimerEventTrigger trigger = new TimerEventTrigger(System.currentTimeMillis());
    //        assertThat(resource.updateTimerEventTrigger(trigger).getExecutionDate()).isEqualTo(date.getTime());
    //    }
}
