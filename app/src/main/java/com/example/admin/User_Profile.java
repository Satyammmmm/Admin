package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {
    TextView name,email,pas;
    String namee,emaill,pass;

    ImageView imageView;
    FirebaseAuth authprofile;

    Button bt,logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pas = findViewById(R.id.pass);
        bt = findViewById(R.id.butt4);
        logout = findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authprofile.signOut();
                Toast.makeText(User_Profile.this, "User LogOut Succefully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(User_Profile.this,Login.class));
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Profile.this,cardview.class));
            }
        });

        authprofile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authprofile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "User details is not Save", Toast.LENGTH_SHORT).show();
        }else {
            showUSerProfile(firebaseUser);
        }
    }

    private void showUSerProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference refrenceProfile = FirebaseDatabase.getInstance().getReference("Resister User");
        refrenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
//                    namee = firebaseUser.getDisplayName();
                    namee = readUserDetails.user;
                    emaill = firebaseUser.getEmail();
                    pass = readUserDetails.pass;

                    name.setText(namee);
                    email.setText(emaill);
                    pas.setText(pass);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(User_Profile.this, "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}