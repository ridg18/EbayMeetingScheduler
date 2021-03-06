package com.raga.meetings.error;

import com.raga.meetings.error.exceptions.AddMeetingException;
import com.raga.meetings.error.exceptions.DeleteMeetingException;
import com.raga.meetings.error.exceptions.MeetingValidatorException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;

/**
 * Created By: raga
 * Date: 06/06/2020
 * Time: 20:27
 */
@ControllerAdvice
@Log4j2
public class MeetingServiceErrorAdvice {


    @ExceptionHandler({DeleteMeetingException.class})
    public ResponseEntity<String> handleDeleteMeetingException(EntityNotFoundException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }


    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException e) {
        return error(NOT_FOUND, e);
    }

    @ExceptionHandler({AddMeetingException.class})
    public ResponseEntity<String> handleAddMeetingException(AddMeetingException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({MeetingValidatorException.class})
    public ResponseEntity<String> handleMeetingValidatorsException(MeetingValidatorException e) {
        return error(BAD_REQUEST, e);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        ApiError apiError = new ApiError(status.value(), status.getReasonPhrase(), e.getMessage());
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(apiError.toString());
    }
}
