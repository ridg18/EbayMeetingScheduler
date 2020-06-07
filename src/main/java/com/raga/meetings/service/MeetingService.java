package com.raga.meetings.service;

import com.google.common.collect.Lists;
import com.raga.meetings.error.exceptions.AddMeetingException;
import com.raga.meetings.error.exceptions.DeleteMeetingException;
import com.raga.meetings.error.exceptions.MeetingValidatorException;
import com.raga.meetings.model.Meeting;
import com.raga.meetings.repository.MeetingRepository;
import com.raga.meetings.validator.MeetingValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TransactionRequiredException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: raga
 * Date: 04/06/2020
 * Time: 12:09
 */
@Service
@Log4j2
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    /**
     * retrieve all meeting from db
     *
     * @return List<Meeting>
     */
    public List<Meeting> findAll() {
        log.debug("retrieve all meetings from db");
        return Lists.newArrayList(meetingRepository.findAll());
    }

    /**
     * delete all meetings in db
     * (this method used for tests)
     */
    public void deleteAll() {
        log.debug("delete all meetings from db");
        meetingRepository.deleteAll();
    }

    /**
     * the method retrieve all meetings from db with the inserted title and delete it.
     * the method throws EntityNotFoundException
     *
     * @param title
     */
    public void removeMeetingByTitle(String title) throws DeleteMeetingException {
        log.debug("retrieve all meetings from db with title: " + title);
        Iterable<Meeting> meetingsToDelete = meetingRepository.findAllByTitle(title);
        if (Lists.newArrayList(meetingsToDelete).isEmpty()) {
            log.error("Meetings with tile " + title + " not found");
            throw new EntityNotFoundException("Meetings with tile " + title + " not found");
        }
        try {
            log.debug("delete meeting: " + meetingsToDelete);
            meetingRepository.deleteAll(meetingsToDelete);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error("Couldn't delete meeting with title: " + title);
            throw new DeleteMeetingException("Couldn't delete meeting with title: " + title);
        }
    }

    /**
     * the method retrieve from db  meeting with the inserted fromTime and delete it.
     * the method throws EntityNotFoundException
     *
     * @param fromTime
     */
    public void removeMeetingByFromTime(LocalDateTime fromTime) throws DeleteMeetingException {
        log.debug("retrieve meeting from db with start date and time: " + fromTime);
        Meeting meetingToDelete = meetingRepository.findByFromTime(fromTime);
        try {
            log.debug("delete meeting: " + meetingToDelete);
            meetingRepository.delete(meetingToDelete);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error("Couldn't delete meeting with fromTime: " + fromTime);
            throw new DeleteMeetingException("Couldn't delete meeting with title: " + fromTime);
        }
    }

    /**
     * add meeting to the db after validation
     * the method throws AddMeetingException
     *
     * @param meeting
     */
    public Meeting addMeeting(Meeting meeting) throws AddMeetingException, MeetingValidatorException {
        log.debug("retrieve all meetings from db");
        Iterable<Meeting> meetings = meetingRepository.findAll();
        Meeting createdMeeting;
        log.debug("validate meeting");
        MeetingValidator.validateMeeting(meeting, meetings);
        try {
            createdMeeting = meetingRepository.save(meeting);
            log.debug("meeting: " + meeting + " added successfully to db");
            return createdMeeting;
        } catch (Exception e) {
            log.error("Couldn't add meeting " + meeting);
            throw new AddMeetingException("Couldn't add meeting " + meeting, e);
        }

    }

    /**
     * search for the next closest meeting,
     * there will be only one because of the overlapping condition.
     */
    public Meeting getNextMeeting() {
        ZoneId zoneId = ZoneId.of("UTC");
        ArrayList<Meeting> meetings = Lists.newArrayList(meetingRepository.findAll());
        if (meetings.isEmpty()) {
            log.error("couldn't find next meeting");
            throw new EntityNotFoundException("couldn't find next meeting");
        }
        Date now = new Date(Instant.now().getEpochSecond() * 1000l);
        long min = Long.MAX_VALUE;
        int index = Integer.MAX_VALUE;
        for (int i = 0; i < meetings.size(); i++) {
            long epoch = meetings.get(i).getFromTime().toLocalDate().atStartOfDay(zoneId).toEpochSecond() * 1000l;
            min = Math.min(min, Math.abs(epoch - now.getTime()));
            index = i;
        }
        if (index != Integer.MAX_VALUE) {
            return meetings.get(index);
        } else {
            log.error("couldn't find next meeting");
            throw new EntityNotFoundException("couldn't find next meeting");
        }

    }
}
