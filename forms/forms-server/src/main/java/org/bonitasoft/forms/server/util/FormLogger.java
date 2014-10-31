package org.bonitasoft.forms.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.forms.client.model.FormFieldValue;

public class FormLogger implements IFormLogger {

    protected Logger LOGGER;

    public FormLogger(final String className) {
        LOGGER = Logger.getLogger(className);
    }

    @Override
    public void log(final Level level, final String message, final Map<String, Object> pcontext) {
        this.log(level, message, null, pcontext);
    }

    @Override
    public void log(final Level level, String message, Throwable e, final Map<String, Object> pcontext) {
        if (e == null) {
            e = new Exception();
        }

        final FormContextUtil ctxu = new FormContextUtil(pcontext);
        String prefixMessage = "";

        if (ctxu.getSession() != null && ctxu.getUserName() != null) {
            prefixMessage += "Username<" + ctxu.getUserName() + "> ";
        }

        if (ctxu.getFormName() != null) {
            prefixMessage += "Form<" + ctxu.getFormName() + "> ";
        }

        final HashMap<String, FormFieldValue> submittedFields = ctxu.getSubmittedFields();
        if (!submittedFields.isEmpty()) {
            prefixMessage += "Submitted Fields<";
            final String fieldStringRepresentation = getFormFieldStringRepresentation("", submittedFields);
            prefixMessage += fieldStringRepresentation + "> ";
        }

        try {
            // in case of process instanciation
            if (ctxu.getProcessDefinitionId() != null) {
                prefixMessage += "Process<" + ctxu.getProcessName();
                if (ctxu.getProcessVersion() != null) {
                    prefixMessage += " " + ctxu.getProcessVersion();
                }
                prefixMessage += "> ";
            } else if (ctxu.getTaskId() != null) {
                prefixMessage += "Task<" + ctxu.getTaskName() + "> ";
            }

        } catch (final ProcessDefinitionNotFoundException e1) {
            prefixMessage += "Process definition not found " + e1.getMessage();
        } catch (final BPMEngineException e1) {
            prefixMessage += e1.getMessage();
        }
        if (message == null) {
            message = "";
        }
        message = prefixMessage;

        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, message, e);
        } else if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, message, e);
        } else if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, message, e);
        } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, message, e);
        }
    }

    protected String getFormFieldStringRepresentation(String returnedStr, final Map<String, FormFieldValue> submittedFields) {
        int i = 0;
        for (final Map.Entry<String, FormFieldValue> entry : submittedFields.entrySet()) {
            i = i + 1;
            final FormFieldValue fieldValue = entry.getValue();
            final String fieldName = entry.getKey();
            if (!entry.getValue().hasChildWidgets()) {
                returnedStr += formatLogField(fieldName, fieldValue);
            }
            if (submittedFields.size() > 1) {
                returnedStr += " ; ";
            }
        }
        return returnedStr;
    }

    protected String formatLogField(final String fieldName, final FormFieldValue fieldValue) {
        return fieldName + " (" + fieldValue.getValueType() + ")" + " => " + fieldValue.getValue();
    }

    @Override
    public boolean isLoggable(final Level level) {
        return LOGGER.isLoggable(level);
    }
}
