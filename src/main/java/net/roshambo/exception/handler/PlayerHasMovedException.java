package net.roshambo.exception.handler;

import net.roshambo.exception.GlobalServiceException;
import org.springframework.http.HttpStatusCode;

public class PlayerHasMovedException extends GlobalServiceException {
    public PlayerHasMovedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
