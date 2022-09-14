package com.example.coursework_2022_2nd.data;

import com.example.coursework_2022_2nd.Constants;

import java.util.Calendar;
import java.util.Date;

public class TripEntity {
    private String id;
    private String name;
    private String destination;
    private String date;
    private int participant;
    private String description;
    private String risk;
    private String transportation;
    private String detail;

    public TripEntity() {
        this(
                Constants.New_Trip_ID,
                Constants.Empty_String,
                Constants.Empty_String,
                Calendar.getInstance().getTime().toString(),
                0,
                Constants.Empty_String,
                Constants.Empty_String,
                Constants.Empty_String,
                Constants.Empty_String
        );
    }
    public TripEntity(String name, String destination, String date, int participant, String description,
                      String risk, String transportation, String detail) {
        this(Constants.New_Trip_ID, name, destination, date, participant, description, risk, transportation, detail);
    }

    public TripEntity(String id, String name, String destination, String date, int participant, String description,
                      String risk, String transportation, String detail) {
        setId(id);
        setName(name);
        setDestination(destination);
        setDate(date);
        setParticipant(participant);
        setDescription(description);
        setRisk(risk);
        setTransportation(transportation);
        setDetail(detail);
    }

    @Override
    public String toString(){
        return "Apartment{" +
                "Id ='" +id+'\'' +
                ", name ='" + name + '\''+
                ", destination ='" +destination+'\'' +
                ", date ='" +date+'\'' +
                ", participant ='" +participant+'\'' +
                ", description ='" +description+'\'' +
                ", risk ='" +risk+'\'' +
                ", transportation ='" +transportation+'\'' +
                ", detail ='" +detail+'\'' +
                '}';
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public int getParticipant() {
        return participant;
    }

    public String getDescription() {
        return description;
    }

    public String getDestination() {
        return destination;
    }

    public String getDetail() {
        return detail;
    }

    public String getName() {
        return name;
    }

    public String getRisk() {
        return risk;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


}
