package com.qwad1000.kpt.da;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.qwad1000.kpt.model.TransportItem;
import com.qwad1000.kpt.model.TransportTypeEnum;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сергій on 02.10.2014.
 */
public class TransportItemDataBaseSource {
    private SQLiteDatabase database;
    private TransportDataBaseHelper dataBaseHelper;
    private String[] allTableColumns = TransportDataBaseHelper.getAllColumns();

    public TransportItemDataBaseSource(Context context) {
        dataBaseHelper = new TransportDataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
    }

    public long addTransportItem(TransportItem transportItem) {
        ContentValues values = new ContentValues();
        values.put(TransportDataBaseHelper.COLUMN_NUMBER, transportItem.getNumber());
        values.put(TransportDataBaseHelper.COLUMN_IS_WEEKEND, transportItem.isWeekend());
        values.put(TransportDataBaseHelper.COLUMN_TRANSPORT_TYPE, transportItem.getType().toInt());
        values.put(TransportDataBaseHelper.COLUMN_URL, transportItem.getUrl().toString());

        long insertId = database.insert(TransportDataBaseHelper.TABLE_TRANSPORTITEMS, null, values);
        return insertId;
    }

    public void deleteTransportItem(TransportItem transportItem) {
        long id = transportItem.getId();
        database.delete(TransportDataBaseHelper.TABLE_TRANSPORTITEMS,
                TransportDataBaseHelper.COLUMN_ID + " = " + id, null);
    }

    public int updateTransportItem(TransportItem transportItem) {
        long id = transportItem.getId();
        ContentValues values = new ContentValues();
        values.put(TransportDataBaseHelper.COLUMN_NUMBER, transportItem.getNumber());
        values.put(TransportDataBaseHelper.COLUMN_IS_WEEKEND, transportItem.isWeekend());
        values.put(TransportDataBaseHelper.COLUMN_TRANSPORT_TYPE, transportItem.getType().toInt());
        values.put(TransportDataBaseHelper.COLUMN_URL, transportItem.getUrl().toString());

        int rowsAffected = database.update(TransportDataBaseHelper.TABLE_TRANSPORTITEMS,
                values, TransportDataBaseHelper.COLUMN_ID + " LIKE ?", new String[]{String.valueOf(id)});

        return rowsAffected;
    }

    @Deprecated
    public TransportItem getTransportItem(long id) {
        return null;
    }

    public List<TransportItem> getTransportItemsByType(TransportTypeEnum typeEnum, boolean isWeekend) {
        List<TransportItem> resultList = new ArrayList<TransportItem>();

        Cursor cursor = database.query(TransportDataBaseHelper.TABLE_TRANSPORTITEMS,
                allTableColumns,
                TransportDataBaseHelper.COLUMN_TRANSPORT_TYPE + " LIKE ? " + " AND " + TransportDataBaseHelper.COLUMN_IS_WEEKEND + " LIKE ? ",
                new String[]{String.valueOf(typeEnum.toInt()), String.valueOf(isWeekend ? 1 : 0)},
                null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TransportItem item = cursorToTransportItem(cursor);
            resultList.add(item);

            cursor.moveToNext();
        }
        return resultList;
    }

    @Deprecated
    public List<TransportItem> getAllTransportItems() {
        return null;
    }

    private TransportItem cursorToTransportItem(Cursor cursor) {
        int id = cursor.getInt(0);
        int typeInt = cursor.getInt(1);
        TransportTypeEnum type = TransportTypeEnum.values()[typeInt];

        String number = cursor.getString(2);
        String urlStr = cursor.getString(3);
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        boolean isWeekend = cursor.getInt(4) > 0; //todo: check it;

        return new TransportItem(id, number, type, url, isWeekend);

    }
}
