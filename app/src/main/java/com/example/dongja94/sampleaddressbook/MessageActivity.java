package com.example.dongja94.sampleaddressbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

public class MessageActivity extends AppCompatActivity {
    public static final String EXTRA_ADDRESS_ID = "addressId";
    public static final String EXTRA_ADDRESS_NAME = "name";

    long addressId;

    EditText messageView;
    RadioGroup typeView;
    ListView listView;
    MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        addressId = intent.getLongExtra(EXTRA_ADDRESS_ID, -1L);
        if (addressId == -1L) {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = intent.getStringExtra(EXTRA_ADDRESS_NAME);
        setTitle("Chatting : " + name);

        messageView = (EditText)findViewById(R.id.edit_message);
        typeView = (RadioGroup)findViewById(R.id.group_type);
        listView = (ListView)findViewById(R.id.listView2);
        mAdapter = new MessageAdapter(this);
        listView.setAdapter(mAdapter);

        Button btn = (Button)findViewById(R.id.btn_input);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageView.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    int type = 0;
                    switch (typeView.getCheckedRadioButtonId()) {
                        case R.id.radio_send :
                            type = DataManager.MESSAGE_TYPE_SEND;
                            break;
                        case R.id.radio_receive :
                            type = DataManager.MESSAGE_TYPE_RECEIVE;
                            break;
                        case R.id.radio_date :
                            message = new Date().toString();
                            type = DataManager.MESSAGE_TYPE_DATE;
                            break;
                    }
                    DataManager.getInstance().insertMessage(addressId, message, type);
                    messageView.setText("");
                    refresh();
                }
            }
        });

        refresh();
    }

    private void refresh() {
        Cursor c = DataManager.getInstance().getMessageList(addressId);
        mAdapter.changeCursor(c);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.changeCursor(null);
    }
}
