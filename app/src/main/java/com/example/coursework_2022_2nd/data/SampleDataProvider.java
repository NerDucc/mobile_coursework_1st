package com.example.coursework_2022_2nd.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SampleDataProvider {
    public static List<TripEntity> getTrips() {
        List<TripEntity> trips = new ArrayList<>();
//        apartments.add(new ApartmentEntity("1", "1", "Hello", "Hello", 2, "Hello", "Hello", "Hello"));
//        apartments.add(new ApartmentEntity("2", "house", "Bonjour", "Hello", 3, "Hello", "Hello", "Hello"));
//        apartments.add(new ApartmentEntity("3", "house", "Salut", "Hello", 4, "Hello", "Hello", "Hello"));
//        apartments.add(new ApartmentEntity("4", "house", "Salut", "Hello", 4, "Hello", "Hello", "Hello"));
        trips.add(new TripEntity("1", "2", "Hello", Calendar.getInstance().getTime().toString(), 2, "Hello","Yes", "Hello", "Hello"));
        trips.add(new TripEntity("2", "3", "Hello", Calendar.getInstance().getTime().toString(), 2, "Hello","Yes", "Hello", "Hello"));
        trips.add(new TripEntity("3", "4", "Hello", Calendar.getInstance().getTime().toString(), 2, "Hello","No", "Hello", "Hello"));
        return trips;
    }
}
