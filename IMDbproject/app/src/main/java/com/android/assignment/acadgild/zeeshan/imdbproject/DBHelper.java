package com.android.assignment.acadgild.zeeshan.imdbproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper
{
    IMDb_model imdb;
    Context context;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DB_IMDB";

    private static final String TABLE_IMDB = "IMDb";
    private static final String COL_MOVIE_ID = "ID";
    private static final String COL_IS_FAVORITE = "FAVORITE";
    private static final String COL_IS_WATCHLIST = "WATCHLIST";

    private static final String[] COLUMNS_IMDB = {COL_MOVIE_ID, COL_IS_FAVORITE, COL_IS_WATCHLIST};

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
         imdb = new IMDb_model();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String str_create_table = "CREATE TABLE " + TABLE_IMDB + " ( "
                + COL_MOVIE_ID + " INTEGER PRIMARY KEY, "
                + COL_IS_FAVORITE + " INTEGER, "
                + COL_IS_WATCHLIST + " INTEGER )";
        db.execSQL(str_create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_IMDB);
        this.onCreate(db);
    }

    public void add_new_entry(int id, int is_favorite, int is_watchlist)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_imdb = new ContentValues();

        values_imdb.put(COL_MOVIE_ID, id);
        values_imdb.put(COL_IS_FAVORITE, is_favorite);
        values_imdb.put(COL_IS_WATCHLIST, is_watchlist);

        db.insert(TABLE_IMDB, null, values_imdb);

        db.close();
    }

    public int update_favorite(int id, int is_favorite)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_imdb = new ContentValues();

        values_imdb.put(COL_IS_FAVORITE, is_favorite);

        int i = db.update(TABLE_IMDB, values_imdb, COL_MOVIE_ID + "=?",
                new String[] {String.valueOf(id)});
        Log.e("Updated int value ", String.valueOf(i));
        db.close();

        return i;
    }

    public int update_watchlist(int id, int is_watchlist)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values_imdb = new ContentValues();

        values_imdb.put(COL_IS_WATCHLIST, is_watchlist);

        int i = db.update(TABLE_IMDB, values_imdb, COL_MOVIE_ID + "=?",
                new String[] {String.valueOf(id)});
        db.close();

        return i;
    }

    public int delete_entry(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_IMDB, COL_MOVIE_ID + "=?", new String[] {String.valueOf(id)});

        db.close();
        return i;
    }

    public ArrayList<IMDb_model> get_favorite_entries()
    {
        ArrayList<IMDb_model> arrayList_imdb = new ArrayList<>();

        String str_query = "SELECT * FROM " + TABLE_IMDB + " WHERE " + COL_IS_FAVORITE + " =" +1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(str_query, null);

        IMDb_model imdb = new IMDb_model();
        Log.e("Dbhelper Length ", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst())
        {
            do {
                imdb = new IMDb_model();

                imdb.setMovie_id(Integer.parseInt(cursor.getString(0)));
                imdb.setIs_favorite(Integer.parseInt(cursor.getString(1)));
                imdb.setIs_watchlist(Integer.parseInt(cursor.getString(2)));

                arrayList_imdb.add(imdb);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList_imdb;
    }

    public ArrayList<IMDb_model> get_watchlist_entries()
    {
        ArrayList<IMDb_model> arrayList_imdb = new ArrayList<>();

        String str_query = "SELECT * FROM " + TABLE_IMDB + " WHERE " + COL_IS_WATCHLIST + " = " +1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(str_query, null);

        IMDb_model imdb = null;

        if (cursor.moveToFirst())
        {
            do {
                imdb = new IMDb_model();

                imdb.setMovie_id(Integer.parseInt(cursor.getString(0)));
                imdb.setIs_favorite(Integer.parseInt(cursor.getString(1)));
                imdb.setIs_watchlist(Integer.parseInt(cursor.getString(2)));

                arrayList_imdb.add(imdb);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList_imdb;
    }

    public boolean MovieIDExist(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String str_query = "SELECT * FROM " + TABLE_IMDB + " WHERE " + COL_MOVIE_ID + " = " + id;
        Cursor cursor = db.rawQuery(str_query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public IMDb_model get_entry(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IMDB, COLUMNS_IMDB, COL_MOVIE_ID + " =?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
        IMDb_model imdb = null;

        if ((cursor != null) && (cursor.getCount()>0))
        {
            cursor.moveToFirst();
            imdb = new IMDb_model();
            Log.e("cursor", String.valueOf(cursor.getString(0)));
            imdb.setMovie_id(Integer.parseInt(cursor.getString(0)));
            imdb.setIs_favorite(Integer.parseInt(cursor.getString(1)));
            imdb.setIs_watchlist(Integer.parseInt(cursor.getString(2)));
        }

        if (cursor != null) {
            cursor.close();
        }
        return imdb;

    }
}
