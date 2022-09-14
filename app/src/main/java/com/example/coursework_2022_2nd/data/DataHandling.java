package com.example.coursework_2022_2nd.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataHandling {
    public static String getTimeDate(Date timestamp){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.getDefault());
            return sdf.format(timestamp);
        }
        catch (Exception e){
            return "date";
        }
    }
}
