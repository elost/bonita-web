package org.bonitasoft.console.common.server.login.impl.jaas.cas;


import org.bonitasoft.engine.exception.TenantStatusException;

public class TenantIsPausedRedirectionToMaintenancePageException extends TenantStatusException {

    private static final long serialVersionUID = 4836182063504328577L;

    private final long tenantId;

    public TenantIsPausedRedirectionToMaintenancePageException(final String message, final long tenantId) {
        super(message);
        this.tenantId = tenantId;
    }

    public long getTenantId() {
        return tenantId;
    }

}
