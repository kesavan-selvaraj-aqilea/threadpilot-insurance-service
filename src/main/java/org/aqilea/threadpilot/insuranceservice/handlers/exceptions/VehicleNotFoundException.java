package org.aqilea.threadpilot.insuranceservice.handlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super("Vehicle with Number '" + message + "' not found.");
    }
}
