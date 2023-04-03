package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    TextView t;
    Button tree;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        t = findViewById(R.id.t1);
        tree = findViewById(R.id.butt3);

        tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, User_Profile.class));
            }
        });

    }


    private void checkuserStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("lohinDataa", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("loginCounter", false);
        String email = sharedPreferences.getString("email", "");
        if (counter) {
            t.setText("Login");
        } else {
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
        }
    }
}
