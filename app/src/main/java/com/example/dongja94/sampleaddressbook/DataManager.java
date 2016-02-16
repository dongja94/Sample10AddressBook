package com.example.dongja94.sampleaddressbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
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
    private static final int DB_VERSION = 1;

    private DataManager() {
        super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ DBContant.AddressBook.TABLE_NAME+"(" +
                DBContant.AddressBook._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContant.AddressBook.COLUMN_NAME+" TEXT NOT NULL," +
                DBContant.AddressBook.COLUMN_PHONE+" TEXT," +
                DBContant.AddressBook.COLUMN_HOME+" TEXT," +
                DBContant.AddressBook.COLUMN_OFFICE+" TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAddressCursor(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DBContant.AddressBook._ID,
                DBContant.AddressBook.COLUMN_NAME,
                DBContant.AddressBook.COLUMN_PHONE,
                DBContant.AddressBook.COLUMN_HOME,
                DBContant.AddressBook.COLUMN_OFFICE};
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.isEmpty(keyword)) {
            selection = DBContant.AddressBook.COLUMN_NAME + " LIKE ? OR "+ DBContant.AddressBook.COLUMN_HOME+" LIKE ?";
            selectionArgs = new String[]{"%"+keyword+"%", "%" + keyword + "%"};
        }
        String groupBy = null;
        String having = null;
        String orderBy = DBContant.AddressBook.COLUMN_NAME+" COLLATE LOCALIZED ASC";
        Cursor c = db.query(DBContant.AddressBook.TABLE_NAME,columns,selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }

    public List<AddressData> getAddressList(String keyword) {
        List<AddressData> list = new ArrayList<AddressData>();
        Cursor c = getAddressCursor(keyword);
        int idIndex = c.getColumnIndex(DBContant.AddressBook._ID);
        int nameIndex = c.getColumnIndex(DBContant.AddressBook.COLUMN_NAME);
        int phoneIndex = c.getColumnIndex(DBContant.AddressBook.COLUMN_PHONE);
        int homeIndex = c.getColumnIndex(DBContant.AddressBook.COLUMN_HOME);
        int officeIndex = c.getColumnIndex(DBContant.AddressBook.COLUMN_OFFICE);
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
            values.put(DBContant.AddressBook.COLUMN_NAME, data.name);
            values.put(DBContant.AddressBook.COLUMN_PHONE, data.phone);
            values.put(DBContant.AddressBook.COLUMN_HOME, data.home);
            values.put(DBContant.AddressBook.COLUMN_OFFICE, data.office);
            db.insert(DBContant.AddressBook.TABLE_NAME, null, values);
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
        values.put(DBContant.AddressBook.COLUMN_NAME, data.name);
        values.put(DBContant.AddressBook.COLUMN_PHONE, data.phone);
        values.put(DBContant.AddressBook.COLUMN_HOME, data.home);
        values.put(DBContant.AddressBook.COLUMN_OFFICE, data.office);

        String where = DBContant.AddressBook._ID  + " = ?";
        String[] args = new String[]{""+data._id};
        db.update(DBContant.AddressBook.TABLE_NAME, values, where,args);
    }

    public void deleteAddress(AddressData data) {
        if (data._id == AddressData.INVALID_ID) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        String where = DBContant.AddressBook._ID + " = ?";
        String[] args = new String[]{""+data._id};
        db.delete(DBContant.AddressBook.TABLE_NAME, where, args);
    }
}
