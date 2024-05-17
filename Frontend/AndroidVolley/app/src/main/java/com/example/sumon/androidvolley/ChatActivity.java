package com.example.sumon.androidvolley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private String userName;
    private TextView chatName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatName = (TextView)findViewById(R.id.chatName);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("name");
            chatName.setText("Chat with " + userName);
        }



    }




}