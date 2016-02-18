package com.example.dongja94.sampleaddressbook;

import android.provider.BaseColumns;

/**
 * Created by dongja94 on 2016-02-16.
 */
public class DBContants {
    public interface AddressBook extends BaseColumns {
        public static final String TABLE_NAME = "addressBook";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_OFFICE = "office";
        public static final String COLUMN_LAST_MESSAGE_ID = "lastMessageId";
    }

    public interface MessageTable extends BaseColumns {
        public static final String TABLE_NAME = "messageTable";
        public static final String COLUMN_MATE = "mate";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CREATED_DATE = "created";
    }
}
