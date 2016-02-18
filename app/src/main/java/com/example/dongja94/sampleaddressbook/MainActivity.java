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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    EditText keywordView;
    ListView listView;
    AddressAdapter mAdapter;
    SimpleCursorAdapter mCursorAdapter;

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

        String[] from = {DBContant.AddressBook.COLUMN_NAME, DBContant.AddressBook.COLUMN_PHONE,
                DBContant.AddressBook.COLUMN_HOME, DBContant.AddressBook.COLUMN_OFFICE};
        int[] to = {R.id.text_name, R.id.text_phone, R.id.text_home, R.id.text_office};

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.view_address, null, from , to, 0);
//        listView.setAdapter(mAdapter);

        listView.setAdapter(mCursorAdapter);

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
    public void setData() {
//        List<AddressData> list = DataManager.getInstance().getAddressList(mKeyword);
//        mAdapter.clear();
//        mAdapter.addAll(list);
        Cursor c = DataManager.getInstance().getAddressCursor(mKeyword);
        mCursorAdapter.changeCursor(c);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursorAdapter.changeCursor(null);
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
