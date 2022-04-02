package one.captl.RESTful.notification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {DefaultExceptionHandler.class})
    public ResponseEntity<Object> handleApiException(DefaultExceptionHandler e){
    // create payload containing exception details
        ApiException apiException =new ApiException(
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")),
                HttpStatus.BAD_REQUEST
            );
    return new ResponseEntity<>(apiException,HttpStatus.BAD_REQUEST);
    }
}
