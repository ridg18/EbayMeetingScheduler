package com.raga.meetings.error.exceptions;

import com.raga.meetings.error.ApiError;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: raga
 * Date: 07/06/2020
 * Time: 1:37
 */
@Data
public class MeetingValidatorException extends Exception{
    private List<ApiError> errors;

    public MeetingValidatorException() {
        this.errors = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "MeetingValidatorException{" +
                "" + errors +
                '}';
    }
}
