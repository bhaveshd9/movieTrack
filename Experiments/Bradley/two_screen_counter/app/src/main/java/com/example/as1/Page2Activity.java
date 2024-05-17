package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Page2Activity extends AppCompatActivity {

    Button increaseBtn;
    Button backBtn;
    Button subByTwoBtn;
    TextView numberTxt;

    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        increaseBtn = findViewById(R.id.increaseBtn);
        subByTwoBtn = findViewById(R.id.subByTwo);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number2);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                numberTxt.setText(String.valueOf(++counter));
            }
        });

        subByTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                counter -= 2;
                numberTxt.setText(String.valueOf(counter));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Page2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}