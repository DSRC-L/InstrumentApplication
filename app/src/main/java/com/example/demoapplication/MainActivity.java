package com.example.demoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private InstrumentView instrumentView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instrumentView=findViewById(R.id.myview);
        textView=findViewById(R.id.textView);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                instrumentView.setCurrentProgress(90);

            }
        });


    }






}