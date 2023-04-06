package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email, pass;
    Button login,one;
    private FirebaseAuth authprofile;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        one = findViewById(R.id.butt);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Home.class));
            }
        });

        authprofile = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uemail = email.getText().toString().trim();
                String upas = pass.getText().toString().trim();

                if (TextUtils.isEmpty(uemail)){
                    Toast.makeText(Login.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                }else {
                    loginUser(uemail,upas);

                }
            }
        });


    }

    private void loginUser(String uemail, String upas) {
        authprofile.signInWithEmailAndPassword(uemail,upas).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = authprofile.getCurrentUser();
                    if (!firebaseUser.isEmailVerified()){
//                        Toast.makeText(Login.this, "Login Succesfull now", Toast.LENGTH_SHORT).show();
                        savdata(uemail,upas);
                        startActivity(new Intent(Login.this,Home.class));
                        finish();

                    }else {
                        firebaseUser.sendEmailVerification();
                        authprofile.signOut();
                        showalertDiologBox();
                    }

                }else {
                    Toast.makeText(Login.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showalertDiologBox() {
        AlertDialog.Builder builder= new AlertDialog.Builder(Login.this);
        builder.setTitle("Email not verified");
        builder.setTitle("Plese verify Your Email");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //User already login (Shred prefrence)
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (authprofile.getCurrentUser() != null ){
//            Toast.makeText(this, "You Are loged in Already", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(Login.this, Home.class));
//            finish();
//        }else {
//            Toast.makeText(this, " Please Login...!",Toast.LENGTH_SHORT).show();
//        }
//    }
    void savdata(String uemail,String upas){
        SharedPreferences sharedPreferences = getSharedPreferences("lohinDataa",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginCounter",true);
        editor.putString("email",uemail);
        editor.putString("email",uemail);
        editor.putString("pass",upas);
        editor.apply();
    }
}