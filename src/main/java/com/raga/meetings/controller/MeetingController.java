package com.raga.meetings.controller;

import com.raga.meetings.error.exceptions.AddMeetingException;
import com.raga.meetings.error.exceptions.DeleteMeetingException;
import com.raga.meetings.error.exceptions.MeetingValidatorException;
import com.raga.meetings.model.Meeting;
import com.raga.meetings.service.MeetingService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created By: raga
 * Date: 04/06/2020
 * Time: 12:04
 */
@RestController
@RequestMapping(path = "/meetings")
@Log4j2
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/next")
    public Meeting getNextMeeting() {
        log.info("get next meeting");
        return meetingService.getNextMeeting();
    }

    @DeleteMapping("/title/{meetingTitle}")
    public ResponseEntity<String> RemoveMeeting(@PathVariable @NonNull String meetingTitle) throws DeleteMeetingException {
        log.info("remove meetings with title: "+meetingTitle);
        meetingService.removeMeetingByTitle(meetingTitle);
        return new ResponseEntity<>("Meetings with title \"" + meetingTitle + "\" deleted successfully", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<String> RemoveMeeting(@RequestParam("fromTime")
                                                @DateTimeFormat(pattern = "yyyyMMddHHmm") @NonNull LocalDateTime fromTime) throws DeleteMeetingException {
        log.info("remove meeting start at: "+fromTime);
        meetingService.removeMeetingByFromTime(fromTime);
        return new ResponseEntity<>("Meetings that end at  \"" + fromTime + "\" deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Meeting> setMeeting(@RequestParam @NonNull Meeting meeting) throws AddMeetingException, MeetingValidatorException {
        log.info("create meeting: "+meeting);
        return new ResponseEntity<>(meetingService.addMeeting(meeting), HttpStatus.CREATED);
    }

}
