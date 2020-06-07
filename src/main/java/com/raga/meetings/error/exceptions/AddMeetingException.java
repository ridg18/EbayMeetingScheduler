package com.raga.meetings.error.exceptions;

/**
 * Created By: raga
 * Date: 06/06/2020
 * Time: 21:33
 */
public class AddMeetingException extends Exception {
    public AddMeetingException(String message) {
        super(message);
    }

    public AddMeetingException(String message, Throwable cause) {
        super(message, cause);
    }
}
