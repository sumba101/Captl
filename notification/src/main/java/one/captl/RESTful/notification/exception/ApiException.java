package one.captl.RESTful.notification.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
public class ApiException {
    private final String message;
    private final ZonedDateTime timestamp;
    private final HttpStatus httpStatus;

    public ApiException(String message, ZonedDateTime timestamp, HttpStatus httpStatus) {
        this.message = message;
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
    }
}
