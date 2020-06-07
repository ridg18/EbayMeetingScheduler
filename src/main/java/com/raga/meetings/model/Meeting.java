package com.raga.meetings.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created By: raga
 * Date: 04/06/2020
 * Time: 12:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties
public class Meeting {

    @Id
    @GeneratedValue
    private UUID Id;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private String meetingTitle;

    public Meeting(String meetingTitle, LocalDateTime fromTime, LocalDateTime toTime) {
        this.meetingTitle = meetingTitle;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}
