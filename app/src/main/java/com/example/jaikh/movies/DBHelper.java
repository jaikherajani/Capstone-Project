package com.example.jaikh.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jaikh on 23-04-2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="popmovies.db";
    private static final int SCHEMA=1;
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favmovies (movie_id LONG PRIMARY KEY,poster_path TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String poster_path,long movie_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Insert data in table
        ContentValues contentValues = new ContentValues();
        contentValues.put("poster_path",poster_path);
        contentValues.put("movie_id", movie_id);
        long result = db.insert("favmovies", null, contentValues);

        db.close();
        if (result == -1)
            return false;
        else
            return true;

    }
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        //Get data from table
        String query ="SELECT * FROM FAVMOVIES;";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
