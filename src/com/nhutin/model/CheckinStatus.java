/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhutin.model;

import java.time.LocalDateTime;

/**
 *
 * @author MaiVuLuan
 */
public class CheckinStatus {

    private int checkinID;
    private int participantID;
    private int eventID;
    private LocalDateTime checkinTime;
    private boolean status;
 
    public CheckinStatus() {
    }

    public CheckinStatus(int checkinID, int participantID, int eventID, LocalDateTime checkinTime, boolean status) {
        this.checkinID = checkinID;
        this.participantID = participantID;
        this.eventID = eventID;
        this.checkinTime = checkinTime;
        this.status = status;
    }

    public int getCheckinID() {
        return checkinID;
    }

    public void setCheckinID(int checkinID) {
        this.checkinID = checkinID;
    }

    public int getParticipantID() {
        return participantID;
    }

    public void setParticipantID(int participantID) {
        this.participantID = participantID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

  
}
