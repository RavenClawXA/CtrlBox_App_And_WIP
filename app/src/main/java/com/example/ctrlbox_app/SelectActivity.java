package com.example.ctrlbox_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {
    Button btn_ctrl, btn_wip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        btn_ctrl = findViewById(R.id.btn_ctrl);
        btn_wip = findViewById(R.id.btn_wip);

        Intent rec = getIntent();
        String Username = rec.getStringExtra("USERNAME");


        btn_ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SelectActivity.this,ScanActivity.class);
                startActivity(intent1);
            }
        });

        btn_wip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent senderIntent = new Intent(SelectActivity.this, WipActivity.class);
                senderIntent.putExtra("USERNAME",Username);
                startActivity(senderIntent);
            }
        });
    }
}