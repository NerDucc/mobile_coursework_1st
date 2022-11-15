package com.example.coursework_2022_2nd.data;

import com.example.coursework_2022_2nd.Constants;

import org.json.JSONObject;

import java.util.HashMap;

public class ExpenseEntity {

    private String E_ID;
    private String type;
    private String date;
    private String notes;
    private Integer amount;
    private String location;
    private String T_ID;

    public ExpenseEntity( String type, String date, String notes, Integer amount, String location ,String T_ID) {
        this.E_ID = Constants.New_Trip_ID;
        this.type = type;
        this.date = date;
        this.notes = notes;
        this.amount = amount;
        this.location = location;
        this.T_ID = T_ID;
    }

    public String getT_ID() {
        return T_ID;
    }

    public void setT_ID(String t_ID) {
        T_ID = t_ID;
    }

    public ExpenseEntity(String e_ID, String type, String date, String notes, Integer amount,String location ,String T_ID) {
        setE_ID(e_ID);
        setType(type);
        setAmount(amount);
        setDate(date);
        setNotes(notes);
        setLocation(location);
        setT_ID(T_ID);
    }
    public ExpenseEntity() {
        this(
                Constants.New_Trip_ID,
                Constants.Empty_String,
                Constants.Empty_String,
                Constants.Empty_String,
                0,
                Constants.Empty_String,
                Constants.Empty_String
        );
    }




    public String getE_ID() {
        return E_ID;
    }

    public void setE_ID(String e_ID) {
        E_ID = e_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString(){
        return "Apartment{" +
                "Id ='" +E_ID+'\'' +
                ", name ='" + type + '\''+
                ", destination ='" +amount+'\'' +
                ", date ='" +date+'\'' +
                ", notes ='" +notes +
                        '}';
    }
    public JSONObject toMap(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", type);
        map.put("description", notes);
        map.put("date", date);
        map.put("location", location);
        map.put("amount", amount);
        JSONObject detail = new JSONObject(map);
        return detail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
