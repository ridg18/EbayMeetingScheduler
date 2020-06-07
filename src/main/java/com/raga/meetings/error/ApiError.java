package com.raga.meetings.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Created By: raga
 * Date: 06/06/2020
 * Time: 20:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private int status;
    private String error;
    private String message;

    public ApiError(HttpStatus httpStatus, String message){
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }


    @Override
    public String toString() {
        return "{" + "\n" +
                "\tstatus = " + status + ",\n" +
                "\terror = " + error + ",\n" +
                "\tmessage = '" + message + "\'\n" +
                '}';
    }
}
