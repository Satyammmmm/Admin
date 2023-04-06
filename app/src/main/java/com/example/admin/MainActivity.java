package com.example.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.R;
import com.example.admin.ReadWriteUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

    EditText user,email,pass;
    Button login,log,one;
    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login= findViewById(R.id.login);
        log= findViewById(R.id.log);
        one= findViewById(R.id.button);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = user.getText().toString().trim();
                String uemail = email.getText().toString().trim();
                String upassword = pass.getText().toString().trim();

                registureuser(username,uemail,upassword);
            }
        });


    }

    private void registureuser(String username, String uemail, String upassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(uemail,upassword).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(username,uemail,upassword);

                            DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Resister User");
//                            SharedPreferences sharedPreferences = getSharedPreferences("Ru", MODE_PRIVATE);
//                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                            myEdit.putString("name", referenceprofile.toString());
//                            myEdit.commit();
//                                    assert firebaseUser != null;
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(MainActivity.this, "User regestre Succesfull, plese verify email", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(MainActivity.this,Login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(MainActivity.this, "User regestre Faild, plese verify Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });


        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
    }
}