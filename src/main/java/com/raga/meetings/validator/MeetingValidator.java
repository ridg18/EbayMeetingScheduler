package com.raga.meetings.validator;

import com.raga.meetings.error.ApiError;
import com.raga.meetings.error.exceptions.MeetingValidatorException;
import com.raga.meetings.model.Meeting;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created By: raga
 * Date: 05/06/2020
 * Time: 0:15
 */
public class MeetingValidator {


    /**
     * the method validate that the meeting not set to SATURDAY.
     * if the meeting on SATURDAY it will add BAD_REQUEST to the meetingValidatorException.
     *
     * @param meet
     * @param meetingValidatorException
     * @return true if validate else false
     */
    public static boolean validateMeetingNotOnSaturday(Meeting meet, MeetingValidatorException meetingValidatorException) {
        if (meet.getFromTime().getDayOfWeek() == DayOfWeek.SATURDAY ||
                meet.getToTime().getDayOfWeek() == DayOfWeek.SATURDAY) {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "Can't add meeting on Saturday"));
            return false;
        } else {
            return true;
        }

    }

    /**
     * the method validate that the meeting time and date not passed yet.
     * if the meeting's time passed it will add BAD_REQUEST to the meetingValidatorException.
     *
     * @param meet
     * @param meetingValidatorException
     * @return true if validate else false
     */
    public static boolean validateMeetingTimeDidNotPassed(Meeting meet, MeetingValidatorException meetingValidatorException) {
        if (meet.getFromTime().toLocalDate().isBefore(LocalDateTime.now().toLocalDate()) ||
                ((meet.getFromTime().toLocalDate().isEqual(LocalDateTime.now().toLocalDate())
                        && meet.getFromTime().toLocalTime().isBefore(LocalDateTime.now().toLocalTime())))) {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "Can't add Passed meeting"));
            return false;
        } else {
            return true;
        }

    }


    /**
     * the method validate that the meeting start time is not after end time .
     * if the meeting start date is after end date it will add BAD_REQUEST to the meetingValidatorException.
     *
     * @param meet
     * @param meetingValidatorException
     * @return true if validate else false
     */
    public static boolean validateStartTimeNotAfterToTime(Meeting meet, MeetingValidatorException meetingValidatorException) {
        if (meet.getFromTime().toLocalTime().isAfter(meet.getToTime().toLocalTime()) ||
                meet.getFromTime().toLocalDate().isAfter(meet.getToTime().toLocalDate())) {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "meeting start time can't be after end time"));
            return false;
        } else {
            return true;
        }

    }

    /**
     * the method validate that two meeting do not overlap
     *
     * @param meet
     * @param meetings
     * @param meetingValidatorException
     * @return
     */
    public static boolean isOverlapping(Meeting meet, Iterable<Meeting> meetings, MeetingValidatorException meetingValidatorException) {

        for (Meeting m : meetings) {
            if (meet.getFromTime().toLocalDate().equals(m.getFromTime().toLocalDate())) {

                if (meet.getFromTime().toLocalTime().isAfter(m.getFromTime().toLocalTime())
                        && meet.getFromTime().toLocalTime().isBefore(m.getToTime().toLocalTime())) {
                    meetingValidatorException.getErrors()
                            .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting start Time overlap another meeting"));
                }
                if (meet.getToTime().toLocalTime().isAfter(m.getFromTime().toLocalTime())
                        && meet.getToTime().toLocalTime().isBefore(m.getToTime().toLocalTime())) {
                    meetingValidatorException.getErrors()
                            .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting end Time overlap another meeting"));
                }

                if (meet.getFromTime().toLocalTime().isBefore(m.getFromTime().toLocalTime())
                        && meet.getToTime().toLocalTime().isAfter(m.getToTime().toLocalTime())) {
                    meetingValidatorException.getErrors()
                            .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting end Time overlap another meeting"));
                }
            }
        }
        return false;
    }

    /**
     * validate meeting duration is more or equal to 15 minutes and less or equal to 2 hours
     *
     * @param meet
     * @param meetingValidatorException
     * @return
     */
    public static boolean validateMeetingLess2HoursMore15Minutes(Meeting meet, MeetingValidatorException meetingValidatorException) {
        Long meetingDuration = Duration.between(meet.getFromTime(), meet.getToTime()).getSeconds();
        if (meetingDuration <= 7200 && meetingDuration >= 900) {
            return true;
        } else {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting can't be more than 2 hours and less than 15 minutes"));
            return false;
        }

    }

    /**
     * validate that the sum of all meetings duration in the week do not exceed 40 hours
     *
     * @param meet
     * @param meetingValidatorException
     * @return
     */
    public static boolean validateNotExceeding40WeeklyHours(Meeting meet, Iterable<Meeting> meetings,
                                                            MeetingValidatorException meetingValidatorException) {
        Long count = 0L;
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        for (Meeting m : meetings) {
            if (meet.getFromTime().toLocalDate().get(weekFields.weekOfWeekBasedYear()) ==
                    m.getFromTime().toLocalDate().get(weekFields.weekOfWeekBasedYear())) {

                count += m.getFromTime().toLocalTime().until(m.getToTime().toLocalTime(), ChronoUnit.HOURS);
            }
        }
        if (((count + meet.getFromTime().toLocalTime().until(meet.getToTime().toLocalTime(), ChronoUnit.HOURS) <
                TimeUnit.HOURS.toHours(40)))) {
            return true;
        } else {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting can't exceed 40 weekly hours"));
            return false;
        }
    }

    /**
     * validate that the sum of all meetings duration in the week do not exceed 40 hours
     *
     * @param meet
     * @param meetingValidatorException
     * @return
     */
    public static boolean validateNotExceeding10DailyHours(Meeting meet, Iterable<Meeting> meetings,
                                                           MeetingValidatorException meetingValidatorException) {

        Long count = 0L;
        for (Meeting m : meetings) {
            if (meet.getFromTime().toLocalDate().equals(m.getFromTime().toLocalDate())) {
                count += m.getFromTime().toLocalTime().until(m.getToTime().toLocalTime(), ChronoUnit.HOURS);
            }
        }
        if ((count + meet.getFromTime().toLocalTime().until(meet.getToTime().toLocalTime(), ChronoUnit.HOURS) <
                TimeUnit.HOURS.toHours(10))) {
            return true;
        } else {
            meetingValidatorException.getErrors()
                    .add(new ApiError(HttpStatus.BAD_REQUEST, "Meeting can't exceed 10 Daily hours"));
            return false;
        }
    }

    public static boolean validateMeeting(Meeting meet, Iterable<Meeting> meetings) throws MeetingValidatorException {
        MeetingValidatorException meetingValidatorException = new MeetingValidatorException();
        isOverlapping(meet, meetings, meetingValidatorException);
        validateMeetingLess2HoursMore15Minutes(meet, meetingValidatorException);
        validateStartTimeNotAfterToTime(meet, meetingValidatorException);
        validateMeetingNotOnSaturday(meet, meetingValidatorException);
        validateNotExceeding40WeeklyHours(meet, meetings, meetingValidatorException);
        validateNotExceeding10DailyHours(meet, meetings, meetingValidatorException);
        validateMeetingTimeDidNotPassed(meet, meetingValidatorException);
        if (meetingValidatorException.getErrors().size() > 0) {
            throw meetingValidatorException;
        }
        return true;

    }

}