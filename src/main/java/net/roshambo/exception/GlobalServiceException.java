package net.roshambo.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalServiceException extends ResponseStatusException {
    public GlobalServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalServiceException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
