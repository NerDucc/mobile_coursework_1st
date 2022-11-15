package com.example.coursework_2022_2nd.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    SQLiteDatabase db;

    public MutableLiveData<TripEntity> trip = new MutableLiveData<>();

    public  MutableLiveData<List<TripEntity>> tripList = new MutableLiveData<List<TripEntity>>();

    public TripDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    @SuppressLint("Range")
    public List<TripEntity> get(String DATABASE_CREATE, String ... selectArgs){
        List<TripEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(DATABASE_CREATE, selectArgs);

        while (cursor.moveToNext()){
            TripEntity tripDB = new TripEntity();
            tripDB.setId(cursor.getString(cursor.getColumnIndex("trip_id")));
            tripDB.setName(cursor.getString(cursor.getColumnIndex("name")));
            tripDB.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
            tripDB.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            tripDB.setTransportation(cursor.getString(cursor.getColumnIndex("transportation")));
            tripDB.setRisk(cursor.getString(cursor.getColumnIndex("risk")));
            tripDB.setDate(cursor.getString(cursor.getColumnIndex("date_trip")));
            tripDB.setParticipant(cursor.getInt(cursor.getColumnIndex("participant")));
            list.add(tripDB);
        }
        return list;
    }

    public List<TripEntity> getAll(){
        String dbSelect = "SELECT * FROM trips";
        return get(dbSelect);
    }

    public TripEntity getByID(String id){
        String dbGetOne = "SELECT * FROM trips WHERE trip_id = ?";

        List<TripEntity> list = get(dbGetOne, id);

        return list.get(0);
    }

    public long insert(TripEntity tripDB){
        ContentValues contentValues = new ContentValues();
//        contentValues.put("trip_id", tripDB.getId());
        contentValues.put("name", tripDB.getName());
        contentValues.put("destination", tripDB.getDestination());
        contentValues.put("date_trip", tripDB.getDate().toString());
        contentValues.put("participant", tripDB.getParticipant());
        contentValues.put("description", tripDB.getDescription());
        contentValues.put("risk", tripDB.getRisk());
        contentValues.put("transportation", tripDB.getTransportation());

        return db.insert( "trips",null,contentValues);
    }

    public long update(TripEntity tripDB){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", tripDB.getName());
        contentValues.put("destination", tripDB.getDestination());
        contentValues.put("date_trip", tripDB.getDate().toString());
        contentValues.put("participant", tripDB.getParticipant());
        contentValues.put("description", tripDB.getDescription());
        contentValues.put("risk", tripDB.getRisk());
        contentValues.put("transportation", tripDB.getTransportation());

        return db.update( "trips",contentValues, "trip_id=?", new String[]{String.valueOf(tripDB.getId())});
    }

    public int delete(String id){
        return db.delete("trips", "trip_id=?", new String[]{String.valueOf(id)});
    }

    public void search(String search){
        String dbSearch = "SELECT * FROM trips WHERE name LIKE ('%" + search + "%')" +
                " OR " + "date_trip LIKE ('%" + search + "%')" +
                " OR " + "destination LIKE ('%" + search + "%')";
        List<TripEntity> list = get(dbSearch);
        tripList.setValue(list);
    }
}
