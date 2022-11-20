package com.example.coursework_2022_2nd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME  = "company";
    private static final String TABLE_NAME = "trips";
    private static final String DROP = "DROP TABLE IF EXISTS ";
    private static final String ID_TRIP = "trip_id";
    private static final String NAME = "name";
    private static final String DESTINATION = "destination";;
    private static final String  PARTICIPANT = "participant";
    private static final String DESCRIPTION = "description";
    private static final String  RISK = "risk";
    private static final String EXPENSE_TABLE = "expense";
    private static final String TRANSPORTATION = "transportation";
    private static final String DATE = "date_trip";
    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT, " + "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s INTEGER)"
            , TABLE_NAME, ID_TRIP,NAME,DESTINATION, DESCRIPTION, TRANSPORTATION, RISK,DATE, PARTICIPANT
                );
    private static final String EXPENSE_CREATE = String.format(
            "CREATE TABLE expense (" +
                    "expense_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "expense_name TEXT, " +
                    "expense_amount INTEGER, " +
                    "expense_date TEXT, " +
                    "expense_comment TEXT, " +
                    "expense_location TEXT, " +
                    "trip_id INTEGER," +
                    "FOREIGN KEY(trip_id) " +
                    "REFERENCES trips(trip_id))");
    private SQLiteDatabase database;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(EXPENSE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL(DROP + TABLE_NAME);
        DB.execSQL(DROP + EXPENSE_TABLE);
        onCreate(DB);
        Log.w(this.getClass().getName(), DATABASE_NAME + "DATABASE UPGRADE TO VERSION " + newVersion + "- OLD DATA LOST");
    }

}
