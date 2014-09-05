/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.server.rest.resources;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;

import java.util.List;

import javax.json.Json;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.Input;
import org.bonitasoft.web.server.rest.utils.BonitaJerseyTest;
import org.junit.Test;
import org.mockito.Mock;

public class TaskResourceTest extends BonitaJerseyTest {

    @Mock
    private ProcessAPI processAPI;
    
    @Override
    protected Application configure() {
        return super.config().register(new TaskResource(processAPI));
    }

    private Entity<List<Input>> someJson() {
        return json(asList(new Input("aBoolean", true)));
    }

    @Test
    public void testName() throws Exception {
        List<Input> inputs = asList(new Input("aBoolean", true), new Input("aString", "hello world"));
        
        Response post = target("tasks/poc/json").request(MediaType.APPLICATION_JSON).post(Entity.json(readFile("expense.json")));
        System.out.println(post.readEntity(String.class));
    }
    
    
}
