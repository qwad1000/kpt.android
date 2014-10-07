package com.qwad1000.kpt.da;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Сергій on 02.10.2014.
 */
public class TransportDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kpt.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRANSPORTITEMS = "transport_items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRANSPORT_TYPE = "type";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IS_WEEKEND = "is_weekend";

    private static final String DATABASE_CREATE = "create table " +
            TABLE_TRANSPORTITEMS + "(" + COLUMN_ID + "integer primary key autoincrement, " +
            COLUMN_TRANSPORT_TYPE + "integer not null, " +
            COLUMN_NUMBER + "text not null, " +
            COLUMN_URL + "text not null, " +
            COLUMN_IS_WEEKEND + "integer not null);";

    public TransportDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TransportDataBaseHelper.class.getName(),
                "Upgrading database from ver. " + oldVersion + " to " + newVersion +
                        "it cause destriying of data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORTITEMS); //warning:
        onCreate(db);
    }

    public static String[] getAllColumns() {
        return new String[]{COLUMN_ID, COLUMN_TRANSPORT_TYPE, COLUMN_NUMBER, COLUMN_URL, COLUMN_IS_WEEKEND};
    }
}
