package com.nhutin.model;

public class Participants {
    private int participantID;
    private int eventID;
    private String firstName;
    private String lastName;
    private String barcode;
    private String email;

    public Participants() {
    }

    public Participants(int participantID, int eventID, String firstName, String lastName, String barcode, String email) {
        this.participantID = participantID;
        this.eventID = eventID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.barcode = barcode;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
