# MeetingScheduler
MeetingScheduler is a tool that allows to  add, remove and retrieve meetings in/from DB.


## Prerequisites

Before you begin, ensure you have met the following requirements:
* You have installed at least java 8.

## Installing MeetingScheduler

To install MeetingScheduler, follow these steps:

1. clone the project from github.
2. clean and install using maven.

## Using MeetingScheduler

To use MeetingScheduler, follow these steps:

1. run the jar.
2. send request with postman

## Assumptions
1. I saved and retrieved all the meetings with UTC time zone, and assumed that the client should convert the time to his timezone.
   If there is need to handle the time zone ion the server side, there will be small change in the interface so the client will send the timezone.
   `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`
   
   
2. I assumed that there is no constraint on the start time of specific day, and the user can schedule his meetings when he wants.

3. The meeting entity don't have user entity, so there is no validations on which or how many users can be on meeting.


## Validations
Each meeting should meet the following validations:

1. Meeting can't last for more than 2 hours and less than 15 minutes.
2. Two meetings can't overlap.
3. No meetings on Saturday.
4. Removing title by title might result in removing more than one meeting.
5. Any meeting for the same week exceeding 40h should be rejected with proper error.
   (I assumed here that if meeting exceed 40 hours weekly it should reject for example if in specific week
   we have 20 meeting and each meeting is 2 hour, and I want to add new meeting ion this week I should get an error).
6. The last meeting on the same day must end up to 10 hours after the first meeting started. Trying to set a meeting that contradicts this
   should be rejected with proper error
 
