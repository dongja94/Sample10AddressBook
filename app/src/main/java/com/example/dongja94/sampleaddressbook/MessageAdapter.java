package com.example.dongja94.sampleaddressbook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dongja94 on 2016-02-18.
 */
public class MessageAdapter extends CursorAdapter {
    public MessageAdapter(Context context) {
        super(context, null, true);
    }

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_INDEX_RECEIVE = 0;
    private static final int VIEW_INDEX_SEND = 1;
    private static final int VIEW_INDEX_DATE = 2;
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor c = (Cursor)getItem(position);
        int type = c.getInt(c.getColumnIndex(DBContants.MessageTable.COLUMN_TYPE));
        switch (type) {
            case DataManager.MESSAGE_TYPE_RECEIVE :
                return VIEW_INDEX_RECEIVE;
            case DataManager.MESSAGE_TYPE_SEND :
                return VIEW_INDEX_SEND;
            case DataManager.MESSAGE_TYPE_DATE :
                return VIEW_INDEX_DATE;
        }
        return VIEW_INDEX_RECEIVE;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int type = cursor.getInt(cursor.getColumnIndex(DBContants.MessageTable.COLUMN_TYPE));
        switch (type) {
            case DataManager.MESSAGE_TYPE_RECEIVE :
                return new ReceiveView(parent.getContext());
            case DataManager.MESSAGE_TYPE_SEND :
                return new SendView(parent.getContext());
            case DataManager.MESSAGE_TYPE_DATE :
                return new DateView(parent.getContext());
        }
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int type = cursor.getInt(cursor.getColumnIndex(DBContants.MessageTable.COLUMN_TYPE));
        String message = cursor.getString(cursor.getColumnIndex(DBContants.MessageTable.COLUMN_MESSAGE));
        switch (type) {
            case DataManager.MESSAGE_TYPE_RECEIVE : {
                ReceiveView receiveView = (ReceiveView)view;
                receiveView.setData(message);
                break;
            }
            case DataManager.MESSAGE_TYPE_SEND : {
                SendView sendView = (SendView)view;
                sendView.setData(message);
                break;
            }
            case DataManager.MESSAGE_TYPE_DATE : {
                DateView dateView = (DateView)view;
                dateView.setData(message);
                break;
            }
        }
    }
}
