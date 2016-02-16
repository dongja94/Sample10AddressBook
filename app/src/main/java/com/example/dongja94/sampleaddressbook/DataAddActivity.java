package com.example.dongja94.sampleaddressbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DataAddActivity extends AppCompatActivity {

    EditText nameView, phoneView, homeView, officeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_add);
        nameView = (EditText)findViewById(R.id.edit_name);
        phoneView = (EditText)findViewById(R.id.edit_phone);
        homeView = (EditText)findViewById(R.id.edit_home);
        officeView = (EditText)findViewById(R.id.edit_office);
        Button btn = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getText().toString();
                String phone = phoneView.getText().toString();
                String home = homeView.getText().toString();
                String office = officeView.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    AddressData data = new AddressData();
                    data.name = name;
                    data.phone = phone;
                    data.home = home;
                    data.office = office;
                    DataManager.getInstance().insertAddress(data);
                    nameView.setText("");
                    phoneView.setText("");
                    homeView.setText("");
                    officeView.setText("");
                }
            }
        });

        btn = (Button)findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
