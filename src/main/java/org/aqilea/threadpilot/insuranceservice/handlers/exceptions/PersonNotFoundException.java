package org.aqilea.threadpilot.insuranceservice.handlers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super("Person with Number '" + message + "' not found.");
    }
}
