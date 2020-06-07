package com.raga.meetings;

import com.raga.meetings.model.Meeting;
import com.raga.meetings.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * Created By: raga
 * Date: 04/06/2020
 * Time: 12:27
 */
//@Component
//public class DummyCLR implements CommandLineRunner {
//
//
//    private final MeetingRepository meetingRepository;
//
//    @Autowired
//    public DummyCLR(MeetingRepository meetingRepository) {
//        this.meetingRepository = meetingRepository;
//
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//
//        Meeting meeting1 = new Meeting("test1", new LocalDateTime(new LocalDate()), new Timestamp(1591315202000L));
//        Meeting meeting2 = new Meeting("test2", new Timestamp(1591340400000L), new Timestamp(1591354800000L));
//        Meeting meeting3 = new Meeting("test3", new Timestamp(1591423200000L), new Timestamp(1591426800000L));
//        Meeting meeting4 = new Meeting("test3", new Timestamp(1591437600000L), new Timestamp(1591444800000L));
//
//        Stream.of(meeting1, meeting2, meeting3, meeting4).forEach(n -> meetingRepository.save(n));
//
//        meetingRepository.findAll().forEach(System.out::println);
//    }
//}
