package com.example.dongja94.sampleaddressbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText keywordView;
    ListView listView;
    AddressAdapter mAdapter;
    SimpleCursorAdapter mCursorAdapter;
    AddressCursorAdapter mAddressCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        keywordView = (EditText)findViewById(R.id.edit_keyword);
        listView = (ListView)findViewById(R.id.listView);
        mAdapter = new AddressAdapter();

        String[] from = {DBContants.AddressBook.COLUMN_NAME, DBContants.AddressBook.COLUMN_PHONE,
                DBContants.AddressBook.COLUMN_HOME, DBContants.AddressBook.COLUMN_OFFICE, DBContants.MessageTable.COLUMN_MESSAGE};
        int[] to = {R.id.text_name, R.id.text_phone, R.id.text_home, R.id.text_office, R.id.text_message};

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.view_address, null, from , to, 0);
//        listView.setAdapter(mAdapter);

        mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == phoneIndex) {
                    TextView phoneView = (TextView)view;
                    String phone = cursor.getString(columnIndex);
                    String prefix = phone.substring(0, 1);
                    StringBuilder sb = new StringBuilder();
                    sb.append(prefix);
                    for (int i = 1; i < phone.length(); i++) {
                        sb.append("*");
                    }
                    phoneView.setText(sb.toString());
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(mCursorAdapter);

        mAddressCursorAdapter = new AddressCursorAdapter(this);
//        listView.setAdapter(mAddressCursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor)listView.getItemAtPosition(position);
                long addressid = c.getLong(c.getColumnIndex(DBContants.AddressBook._ID));
                String name = c.getString(c.getColumnIndex(DBContants.AddressBook.COLUMN_NAME));
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                intent.putExtra(MessageActivity.EXTRA_ADDRESS_ID, addressid);
                intent.putExtra(MessageActivity.EXTRA_ADDRESS_NAME, name);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "name : " + name , Toast.LENGTH_SHORT).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyword = keywordView.getText().toString();
                setData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    String mKeyword = null;
    int phoneIndex = -1;
    public void setData() {
//        List<AddressData> list = DataManager.getInstance().getAddressList(mKeyword);
//        mAdapter.clear();
//        mAdapter.addAll(list);
        Cursor c = DataManager.getInstance().getAddressCursor(mKeyword);
        mCursorAdapter.changeCursor(c);
//        mAddressCursorAdapter.changeCursor(c);
        phoneIndex = c.getColumnIndex(DBContants.AddressBook.COLUMN_PHONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursorAdapter.changeCursor(null);
//        mAddressCursorAdapter.changeCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_data_add) {
            startActivity(new Intent(this, DataAddActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
