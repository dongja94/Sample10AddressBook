package com.example.dongja94.sampleaddressbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dongja94 on 2016-02-16.
 */
public class DataManager extends SQLiteOpenHelper {
    private static DataManager instance;
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    private static final String DB_NAME = "addressbook";
    private static final int DB_VERSION = 2;

    private DataManager() {
        super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ DBContants.AddressBook.TABLE_NAME+"(" +
                DBContants.AddressBook._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContants.AddressBook.COLUMN_NAME+" TEXT NOT NULL," +
                DBContants.AddressBook.COLUMN_PHONE+" TEXT," +
                DBContants.AddressBook.COLUMN_HOME+" TEXT," +
                DBContants.AddressBook.COLUMN_OFFICE+" TEXT," +
                DBContants.AddressBook.COLUMN_LAST_MESSAGE_ID + " INTEGER" +
                ");";
        db.execSQL(sql);

        String messageTable = "CREATE TABLE " + DBContants.MessageTable.TABLE_NAME + "(" +
                DBContants.MessageTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContants.MessageTable.COLUMN_MATE + " INTEGER," +
                DBContants.MessageTable.COLUMN_MESSAGE + " TEXT," +
                DBContants.MessageTable.COLUMN_TYPE + " INTEGER," +
                DBContants.MessageTable.COLUMN_CREATED_DATE + " DATETIME" +
                ");";
        db.execSQL(messageTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            String sql = "ALTER TABLE " + DBContants.AddressBook.TABLE_NAME +
                    " ADD " + DBContants.AddressBook.COLUMN_LAST_MESSAGE_ID + " INTEGER;";
            db.execSQL(sql);
            String messageTable = "CREATE TABLE " + DBContants.MessageTable.TABLE_NAME + "(" +
                    DBContants.MessageTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBContants.MessageTable.COLUMN_MATE + " INTEGER," +
                    DBContants.MessageTable.COLUMN_MESSAGE + " TEXT," +
                    DBContants.MessageTable.COLUMN_TYPE + " INTEGER," +
                    DBContants.MessageTable.COLUMN_CREATED_DATE + " DATETIME" +
                    ");";
            db.execSQL(messageTable);
        }
    }

    public Cursor getMessageList(long addressId) {
        String[] columns = {DBContants.MessageTable.TABLE_NAME +"."+DBContants.MessageTable._ID,
                DBContants.AddressBook.COLUMN_NAME ,
                DBContants.MessageTable.COLUMN_MESSAGE,
                DBContants.MessageTable.COLUMN_TYPE,
                DBContants.MessageTable.COLUMN_CREATED_DATE
                };

        String tableName = DBContants.MessageTable.TABLE_NAME + " INNER JOIN " + DBContants.AddressBook.TABLE_NAME +
                " ON " + DBContants.MessageTable.TABLE_NAME + "."+DBContants.MessageTable.COLUMN_MATE +
                " = " + DBContants.AddressBook.TABLE_NAME + "." + DBContants.AddressBook._ID;
        // messageTable INNER JOIN addressBook ON messageTable.mate = addressBook._id

        String selection = DBContants.MessageTable.COLUMN_MATE + " = ?";
        String[] selectionArgs = {"" + addressId};
        String orderBy = DBContants.MessageTable.COLUMN_CREATED_DATE + " ASC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
        return c;
    }

    public static final int MESSAGE_TYPE_RECEIVE = 0;
    public static final int MESSAGE_TYPE_SEND = 1;
    public static final int MESSAGE_TYPE_DATE = 2;

    public void insertMessage(long addressId, String message, int type) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();
            values.clear();
            values.put(DBContants.MessageTable.COLUMN_MATE, addressId);
            values.put(DBContants.MessageTable.COLUMN_MESSAGE, message);
            values.put(DBContants.MessageTable.COLUMN_TYPE, type);
            Calendar c = Calendar.getInstance();
            values.put(DBContants.MessageTable.COLUMN_CREATED_DATE, c.getTimeInMillis());
            long messageid = db.insert(DBContants.MessageTable.TABLE_NAME, null, values);

            if (type == MESSAGE_TYPE_SEND || type == MESSAGE_TYPE_RECEIVE) {
                values.clear();
                values.put(DBContants.AddressBook.COLUMN_LAST_MESSAGE_ID, messageid);
                String where = DBContants.AddressBook._ID + " = ?";
                String[] args = {"" + addressId};
                db.update(DBContants.AddressBook.TABLE_NAME, values, where, args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getAddressCursor(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DBContants.AddressBook.TABLE_NAME + "." + DBContants.AddressBook._ID,
                DBContants.AddressBook.COLUMN_NAME,
                DBContants.AddressBook.COLUMN_PHONE,
                DBContants.AddressBook.COLUMN_HOME,
                DBContants.AddressBook.COLUMN_OFFICE,
                DBContants.MessageTable.COLUMN_MESSAGE};
        String tableName = DBContants.AddressBook.TABLE_NAME + " LEFT OUTER JOIN " + DBContants.MessageTable.TABLE_NAME +
                " ON " + DBContants.AddressBook.TABLE_NAME + "." + DBContants.AddressBook.COLUMN_LAST_MESSAGE_ID +
                " = " + DBContants.MessageTable.TABLE_NAME + "." + DBContants.MessageTable._ID;
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.isEmpty(keyword)) {
            selection = DBContants.AddressBook.COLUMN_NAME + " LIKE ? OR "+ DBContants.AddressBook.COLUMN_HOME+" LIKE ?";
            selectionArgs = new String[]{"%"+keyword+"%", "%" + keyword + "%"};
        }
        String groupBy = null;
        String having = null;
        String orderBy = DBContants.AddressBook.COLUMN_NAME+" COLLATE LOCALIZED ASC";
        Cursor c = db.query(tableName,columns,selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }

    public List<AddressData> getAddressList(String keyword) {
        List<AddressData> list = new ArrayList<AddressData>();
        Cursor c = getAddressCursor(keyword);
        int idIndex = c.getColumnIndex(DBContants.AddressBook._ID);
        int nameIndex = c.getColumnIndex(DBContants.AddressBook.COLUMN_NAME);
        int phoneIndex = c.getColumnIndex(DBContants.AddressBook.COLUMN_PHONE);
        int homeIndex = c.getColumnIndex(DBContants.AddressBook.COLUMN_HOME);
        int officeIndex = c.getColumnIndex(DBContants.AddressBook.COLUMN_OFFICE);
        while(c.moveToNext()) {
            AddressData data = new AddressData();
            data._id = c.getLong(idIndex);
            data.name = c.getString(nameIndex);
            data.phone = c.getString(phoneIndex);
            data.home = c.getString(homeIndex);
            data.office = c.getString(officeIndex);
            list.add(data);
        }
        c.close();
        return list;
    }

    ContentValues values = new ContentValues();
    public void insertAddress(AddressData data) {
        if (data._id == AddressData.INVALID_ID) {
            SQLiteDatabase db = getWritableDatabase();
            values.clear();
            values.put(DBContants.AddressBook.COLUMN_NAME, data.name);
            values.put(DBContants.AddressBook.COLUMN_PHONE, data.phone);
            values.put(DBContants.AddressBook.COLUMN_HOME, data.home);
            values.put(DBContants.AddressBook.COLUMN_OFFICE, data.office);
            db.insert(DBContants.AddressBook.TABLE_NAME, null, values);
        } else {
            updateAddress(data);
        }
    }

    public void updateAddress(AddressData data) {
        if (data._id == AddressData.INVALID_ID) {
            insertAddress(data);
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        values.clear();
        values.put(DBContants.AddressBook.COLUMN_NAME, data.name);
        values.put(DBContants.AddressBook.COLUMN_PHONE, data.phone);
        values.put(DBContants.AddressBook.COLUMN_HOME, data.home);
        values.put(DBContants.AddressBook.COLUMN_OFFICE, data.office);

        String where = DBContants.AddressBook._ID  + " = ?";
        String[] args = new String[]{""+data._id};
        db.update(DBContants.AddressBook.TABLE_NAME, values, where,args);
    }

    public void deleteAddress(AddressData data) {
        if (data._id == AddressData.INVALID_ID) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        String where = DBContants.AddressBook._ID + " = ?";
        String[] args = new String[]{""+data._id};
        db.delete(DBContants.AddressBook.TABLE_NAME, where, args);
    }
}
