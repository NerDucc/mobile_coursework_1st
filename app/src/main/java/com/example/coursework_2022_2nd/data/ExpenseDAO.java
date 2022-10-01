package com.example.coursework_2022_2nd.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    SQLiteDatabase db;
    String tripID;
    public MutableLiveData<ExpenseEntity> expense = new MutableLiveData<>();

    public  MutableLiveData<List<ExpenseEntity>> expenseList = new MutableLiveData<List<ExpenseEntity>>();

    public ExpenseDAO(Context context, String tID) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        this.tripID = tID;
//        {expenseList.setValue(getAll());}

    }
    @SuppressLint("Range")
    public List<ExpenseEntity> get(String EXPENSE_CREATE, String ... selectArgs){
        List<ExpenseEntity> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(EXPENSE_CREATE, selectArgs);

        while (cursor.moveToNext()){
            ExpenseEntity expenseDB = new ExpenseEntity();
            expenseDB.setE_ID(cursor.getString(cursor.getColumnIndex("expense_id")));
            expenseDB.setType(cursor.getString(cursor.getColumnIndex("expense_name")));
            expenseDB.setDate(cursor.getString(cursor.getColumnIndex("expense_date")));
            expenseDB.setNotes(cursor.getString(cursor.getColumnIndex("expense_comment")));
            expenseDB.setAmount(cursor.getInt(cursor.getColumnIndex("expense_amount")));
            expenseDB.setT_ID(cursor.getString(cursor.getColumnIndex("trip_id")));

            list.add(expenseDB);
        }
        return list;
    }
    public List<ExpenseEntity> getAll(){
        String dbSelect = "SELECT * FROM expense WHERE trip_id =?";
        return get(dbSelect, tripID);
    }
    public ExpenseEntity getByID(String id){
        String dbGetOne = "SELECT * FROM expense WHERE trip_id = ? and expense_id =?";

        List<ExpenseEntity> list = get(dbGetOne,tripID, id);
        expense.setValue(list.get(0));
        return list.get(0);
    }


    public long insert(ExpenseEntity expenseDB){
        ContentValues contentValues = new ContentValues();
        contentValues.put("expense_name", expenseDB.getType());
        contentValues.put("expense_date", expenseDB.getDate());
        contentValues.put("expense_comment", expenseDB.getNotes());
        contentValues.put("expense_amount", expenseDB.getAmount().toString());
        contentValues.put("trip_id", expenseDB.getT_ID());

        return db.insert( "expense",null,contentValues);
    }

    public int update(ExpenseEntity expenseDB){
        ContentValues contentValues = new ContentValues();
        contentValues.put("expense_id", expenseDB.getE_ID());
        contentValues.put("expense_name", expenseDB.getType());
        contentValues.put("expense_date", expenseDB.getDate());
        contentValues.put("expense_comment", expenseDB.getNotes());
        contentValues.put("expense_amount", expenseDB.getAmount().toString());
        return db.update( "expense",contentValues, "expense_id=? and trip_id =?", new String[]{String.valueOf(expenseDB.getE_ID()), tripID});
    }

    public int delete(String id){
        return db.delete("expense", "expense_id=?", new String[]{String.valueOf(id)});
    }


}
