package com.raga.meetings.repository;

import com.raga.meetings.model.Meeting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Created By: raga
 * Date: 04/06/2020
 * Time: 12:03
 */
@Repository
public interface MeetingRepository extends PagingAndSortingRepository<Meeting, Long> {

    @Query("From Meeting m WHERE m.meetingTitle=:title")
    Iterable<Meeting> findAllByTitle(String title);

    @Query("From Meeting m WHERE m.fromTime=:fromTime")
    Meeting findByFromTime(@Param("fromTime") LocalDateTime fromTime);


}
