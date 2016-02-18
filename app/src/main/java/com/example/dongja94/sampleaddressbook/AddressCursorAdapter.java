package com.example.dongja94.sampleaddressbook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dongja94 on 2016-02-18.
 */
public class AddressCursorAdapter extends CursorAdapter {
    public AddressCursorAdapter(Context context) {
        super(context, null, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new AddressView(parent.getContext());
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AddressView aview = (AddressView)view;
        aview.setName(cursor.getString(cursor.getColumnIndex(DBContants.AddressBook.COLUMN_NAME)));
        aview.setPhone(cursor.getString(cursor.getColumnIndex(DBContants.AddressBook.COLUMN_PHONE)));
        aview.setHome(cursor.getString(cursor.getColumnIndex(DBContants.AddressBook.COLUMN_HOME)));
        aview.setOffice(cursor.getString(cursor.getColumnIndex(DBContants.AddressBook.COLUMN_OFFICE)));
    }
}
