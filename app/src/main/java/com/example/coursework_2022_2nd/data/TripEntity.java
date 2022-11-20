package com.example.coursework_2022_2nd.data;

import android.content.Context;

import com.example.coursework_2022_2nd.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TripEntity {
    private String id;
    private String name;
    private String destination;
    private String date;
    private int participant;
    private String description;
    private String risk;
    private String transportation;
    public TripEntity() {
        this(
                Constants.New_Trip_ID,
                Constants.Empty_String,
                Constants.Empty_String,
                Constants.Empty_String,
                0,
                Constants.Empty_String,
                Constants.Empty_String,
                Constants.Empty_String
        );
    }
    public TripEntity(String name, String destination, String date, int participant, String description,
                      String risk, String transportation) {
        this(Constants.New_Trip_ID, name, destination, date, participant, description, risk, transportation);
    }

    public TripEntity(String id, String name, String destination, String date, int participant, String description,
                      String risk, String transportation) {
        setId(id);
        setName(name);
        setDestination(destination);
        setDate(date);
        setParticipant(participant);
        setDescription(description);
        setRisk(risk);
        setTransportation(transportation);
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
                 +
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

    public Map<String, Object> getMapWithoutId(){
        Map<String, Object> bMap = new HashMap<>();
        bMap.put("name", this.name);
        bMap.put("description", this.description);
        bMap.put("destination", this.destination);
        bMap.put("risk", this.risk);
        bMap.put("date", this.date);
        bMap.put("participant", this.participant);
        bMap.put("transportation", this.transportation);
        return bMap;
    }
}
