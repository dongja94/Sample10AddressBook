package com.example.dongja94.sampleaddressbook;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dongja94 on 2016-01-14.
 */
public class ReceiveView extends FrameLayout {
    public ReceiveView(Context context) {
        super(context);
        init();
    }

    ImageView iconView;
    TextView messageView;
    private void init() {
        inflate(getContext(), R.layout.view_receive, this);
        iconView = (ImageView)findViewById(R.id.image_icon);
        messageView = (TextView)findViewById(R.id.text_message);
    }

    public void setData(String message) {
        iconView.setImageResource(R.mipmap.ic_launcher);
        messageView.setText(message);
    }
}
