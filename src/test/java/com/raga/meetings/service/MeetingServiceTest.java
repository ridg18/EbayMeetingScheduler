package com.raga.meetings.service;


import com.raga.meetings.error.exceptions.AddMeetingException;
import com.raga.meetings.error.exceptions.DeleteMeetingException;
import com.raga.meetings.error.exceptions.MeetingValidatorException;
import com.raga.meetings.model.Meeting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created By: raga
 * Date: 06/06/2020
 * Time: 21:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetingServiceTest {

    @Autowired
    MeetingService meetingService;

    @Before
    public void setup(){
        meetingService.deleteAll();
    }


    @Test
    public void addMeeting() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        meetingService.addMeeting(meeting);
        assert (meetingService.findAll().size() == 1);
    }

    @Test
    public void addMeetingOnSaturday() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 13), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 13), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        try {
            meetingService.addMeeting(meeting);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 1);
        }

    }

    @Test
    public void addMeetingOverlap1() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);


        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(11, 10, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);
        try {
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting2);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 1);
        }
    }

    @Test
    public void addMeetingOverlap2() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);


        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 10, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);
        try {
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting2);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 3);
        }
    }

    @Test
    public void addMeetingOverlap3() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);


        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(8, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 10, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);
        try {
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting2);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 2);
        }
    }

    @Test
    public void addMeetingOverlap4() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);


        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(8, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(11, 10, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);
        try {
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting2);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 2);
        }
    }

    @Test
    public void addMeetingExceed40WeeklyHour() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(11, 00, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);

        LocalDateTime startMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(11, 00, 00));
        LocalDateTime endMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(13, 00, 00));
        Meeting meeting1 = new Meeting("test", startMeeting1, endMeeting1);

        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(13, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(15, 0, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);

        LocalDateTime startMeeting3 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(15, 00, 00));
        LocalDateTime endMeeting3 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(17, 00, 00));
        Meeting meeting3 = new Meeting("test", startMeeting3, endMeeting3);

        LocalDateTime startMeeting4 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(17, 00, 00));
        LocalDateTime endMeeting4 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(19, 00, 00));
        Meeting meeting4 = new Meeting("test", startMeeting4, endMeeting4);

        LocalDateTime startMeeting5 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(19, 00, 00));
        LocalDateTime endMeeting5 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(21, 00, 00));
        Meeting meeting5 = new Meeting("test", startMeeting5, endMeeting5);


        LocalDateTime startMeeting6 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting6 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(11, 00, 00));
        Meeting meeting6 = new Meeting("test", startMeeting6, endMeeting6);

        LocalDateTime startMeeting7 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(11, 00, 00));
        LocalDateTime endMeeting7 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(13, 00, 00));
        Meeting meeting7 = new Meeting("test", startMeeting7, endMeeting7);

        LocalDateTime startMeeting8 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(13, 00, 00));
        LocalDateTime endMeeting8 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(15, 0, 00));
        Meeting meeting8 = new Meeting("test", startMeeting8, endMeeting8);

        LocalDateTime startMeeting9 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(15, 00, 00));
        LocalDateTime endMeeting9 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(17, 00, 00));
        Meeting meeting9 = new Meeting("test", startMeeting9, endMeeting9);

        LocalDateTime startMeeting10 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(17, 00, 00));
        LocalDateTime endMeeting10 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(19, 00, 00));
        Meeting meeting10 = new Meeting("test", startMeeting10, endMeeting10);

        LocalDateTime startMeeting11 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(19, 00, 00));
        LocalDateTime endMeeting11 = LocalDateTime.of(LocalDate.of(2020, 6, 9), LocalTime.of(21, 00, 00));
        Meeting meeting11 = new Meeting("test", startMeeting11, endMeeting11);


        LocalDateTime startMeeting12 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting12 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(11, 00, 00));
        Meeting meeting12 = new Meeting("test", startMeeting12, endMeeting12);

        LocalDateTime startMeeting13 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(11, 00, 00));
        LocalDateTime endMeeting13 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(13, 00, 00));
        Meeting meeting13 = new Meeting("test", startMeeting13, endMeeting13);

        LocalDateTime startMeeting14 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(13, 00, 00));
        LocalDateTime endMeeting14 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(15, 0, 00));
        Meeting meeting14 = new Meeting("test", startMeeting14, endMeeting14);

        LocalDateTime startMeeting15 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(15, 00, 00));
        LocalDateTime endMeeting15 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(17, 00, 00));
        Meeting meeting15 = new Meeting("test", startMeeting15, endMeeting15);

        LocalDateTime startMeeting16 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(17, 00, 00));
        LocalDateTime endMeeting16 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(19, 00, 00));
        Meeting meeting16 = new Meeting("test", startMeeting16, endMeeting16);

        LocalDateTime startMeeting17 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(19, 00, 00));
        LocalDateTime endMeeting17 = LocalDateTime.of(LocalDate.of(2020, 6, 10), LocalTime.of(21, 00, 00));
        Meeting meeting17 = new Meeting("test", startMeeting17, endMeeting17);

        LocalDateTime startMeeting18 = LocalDateTime.of(LocalDate.of(2020, 6, 11), LocalTime.of(15, 00, 00));
        LocalDateTime endMeeting18 = LocalDateTime.of(LocalDate.of(2020, 6, 11), LocalTime.of(17, 00, 00));
        Meeting meeting18 = new Meeting("test", startMeeting18, endMeeting18);

        LocalDateTime startMeeting19 = LocalDateTime.of(LocalDate.of(2020, 6, 11), LocalTime.of(17, 00, 00));
        LocalDateTime endMeeting19 = LocalDateTime.of(LocalDate.of(2020, 6, 11), LocalTime.of(19, 00, 00));
        Meeting meeting19 = new Meeting("test", startMeeting19, endMeeting19);


        try {
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting1);
            meetingService.addMeeting(meeting2);
            meetingService.addMeeting(meeting3);
            meetingService.addMeeting(meeting4);
            meetingService.addMeeting(meeting5);
            meetingService.addMeeting(meeting6);
            meetingService.addMeeting(meeting7);
            meetingService.addMeeting(meeting8);
            meetingService.addMeeting(meeting9);
            meetingService.addMeeting(meeting10);
            meetingService.addMeeting(meeting11);
            meetingService.addMeeting(meeting12);
            meetingService.addMeeting(meeting13);
            meetingService.addMeeting(meeting14);
            meetingService.addMeeting(meeting15);
            meetingService.addMeeting(meeting16);
            meetingService.addMeeting(meeting17);
            meetingService.addMeeting(meeting18);
            meetingService.addMeeting(meeting19);
        } catch (MeetingValidatorException e) {
            assert (e.getErrors().size() == 1);
        }
    }

    @Test
    public void addMeetingExceed10DailyHour() throws AddMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(11, 00, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);

        LocalDateTime startMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(11, 00, 00));
        LocalDateTime endMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(13, 00, 00));
        Meeting meeting1 = new Meeting("test", startMeeting1, endMeeting1);

        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(13, 00, 00));
        LocalDateTime endMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(15, 0, 00));
        Meeting meeting2 = new Meeting("test", startMeeting2, endMeeting2);

        LocalDateTime startMeeting3 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(15, 00, 00));
        LocalDateTime endMeeting3 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(17, 00, 00));
        Meeting meeting3 = new Meeting("test", startMeeting3, endMeeting3);

        LocalDateTime startMeeting4 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(17, 00, 00));
        LocalDateTime endMeeting4 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(19, 00, 00));
        Meeting meeting4 = new Meeting("test", startMeeting4, endMeeting4);

        LocalDateTime startMeeting5 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(19, 00, 00));
        LocalDateTime endMeeting5 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(21, 00, 00));
        Meeting meeting5 = new Meeting("test", startMeeting5, endMeeting5);

        try{
            meetingService.addMeeting(meeting);
            meetingService.addMeeting(meeting1);
            meetingService.addMeeting(meeting2);
            meetingService.addMeeting(meeting3);
            meetingService.addMeeting(meeting4);
            meetingService.addMeeting(meeting5);
        }catch (MeetingValidatorException e){
            assert (e.getErrors().size() == 1);
        }
    }

    @Test
    public void deleteMeetingByTitle() throws AddMeetingException, MeetingValidatorException, DeleteMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        meetingService.addMeeting(meeting);
        meetingService.removeMeetingByTitle(meeting.getMeetingTitle());
        assert (meetingService.findAll().size() == 0);
    }

    @Test
    public void deleteMeetingWithWrongTitle() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        meetingService.addMeeting(meeting);
        try{
            meetingService.removeMeetingByTitle("test2");
        }catch (DeleteMeetingException e){
            assert (e.getMessage().equals("Meeting with tile test2 not found"));
        }
    }

    @Test
    public void deleteMeetingByDate() throws AddMeetingException, MeetingValidatorException, DeleteMeetingException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        meetingService.addMeeting(meeting);
        meetingService.removeMeetingByFromTime(meeting.getFromTime());
        assert (meetingService.findAll().size() == 0);
    }

    @Test
    public void deleteMeetingWithWrongDate() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);

        LocalDateTime startMeeting2 = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 00, 00));

        meetingService.addMeeting(meeting);
        try{
            meetingService.removeMeetingByFromTime(startMeeting2);
        }catch (DeleteMeetingException e){
            assert (e.getMessage().equals("Meeting with start time "+startMeeting2+" not found"));
        }
    }

    @Test
    public void getNextMeeting() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);
        meetingService.addMeeting(meeting);

        Meeting meeting2 = meetingService.getNextMeeting();
        assert (meeting2.equals(meeting));
    }


    @Test
    public void getNextMeeting2() throws AddMeetingException, MeetingValidatorException {
        LocalDateTime startMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(9, 00, 00));
        LocalDateTime endMeeting = LocalDateTime.of(LocalDate.of(2020, 6, 7), LocalTime.of(10, 30, 00));
        Meeting meeting = new Meeting("test", startMeeting, endMeeting);

        LocalDateTime startMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(11, 00, 00));
        LocalDateTime endMeeting1 = LocalDateTime.of(LocalDate.of(2020, 6, 8), LocalTime.of(13, 00, 00));
        Meeting meeting1 = new Meeting("test", startMeeting1, endMeeting1);

        meetingService.addMeeting(meeting);
        meetingService.addMeeting(meeting1);

        Meeting meeting3 = meetingService.getNextMeeting();
        assert (meeting3.equals(meeting1));
    }
}
