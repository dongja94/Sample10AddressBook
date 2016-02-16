package com.example.dongja94.sampleaddressbook;

import android.provider.BaseColumns;

/**
 * Created by dongja94 on 2016-02-16.
 */
public class DBContant {
    public interface AddressBook extends BaseColumns {
        public static final String TABLE_NAME = "addressBook";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_HOME = "home";
        public static final String COLUMN_OFFICE = "office";
    }
}
