package org.bonitasoft.web.rest.server.framework.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Fabio Lombardi
 */
public class JacksonSerializer {

    private final ObjectMapper mapper = new ObjectMapper();

    public String serialize(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (final Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
