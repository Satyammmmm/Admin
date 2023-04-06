package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Home extends AppCompatActivity {
    TextView t;
    EditText f_name,address,price,number,email,wapno;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton image;
    ProgressDialog progressDialog;
    private static final int Gallery_Code=1;
    Button tree,upload;
    Uri imageUrl = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        t = findViewById(R.id.t1);
        tree = findViewById(R.id.butt3);
        image = findViewById(R.id.imageButton);

        f_name = findViewById(R.id.EditFristname);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);
        wapno = findViewById(R.id.wapnumber);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);

        upload = findViewById(R.id.btninsert);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("student");
        mStorage=FirebaseStorage.getInstance();




        image.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);

            }
        });


        tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, User_Profile.class));
            }
        });

    }

    private void checkuserStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("lohinDataa", MODE_PRIVATE);
        SharedPreferences sh = getSharedPreferences("Ru", Context.MODE_PRIVATE);
        String s1=sh.getString("name","");
        Toast.makeText(Home.this, s1.toString(), Toast.LENGTH_SHORT).show();


        Boolean counter = sharedPreferences.getBoolean("loginCounter", false);
        String email = sharedPreferences.getString("email", "");
        if (counter) {
            t.setText("Login");
        } else {
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Code && resultCode == RESULT_OK){
            imageUrl=data.getData();
            image.setImageURI(imageUrl);
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = f_name.getText().toString().trim();
                String add = address.getText().toString().trim();
                String pri = price.getText().toString().trim();
                String wpno = wapno.getText().toString().trim();
                String eml = email.getText().toString().trim();
                String num = number.getText().toString().trim();

                if (!(fname.isEmpty() && add.isEmpty() && pri.isEmpty()&& wpno.isEmpty() && eml.isEmpty() && num.isEmpty() && imageUrl != null)){
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    StorageReference filepath =mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloder =taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String  t =task.getResult().toString();
                                    DatabaseReference newPost = mRef.push();
                                    newPost.child("Image").setValue(task.getResult().toString());
                                    newPost.child("Flat Name :-").setValue(fname);
                                    newPost.child("Address :-").setValue(add);
                                    newPost.child("Price :-").setValue(pri);
                                    newPost.child("Whatsaap No :-").setValue(wpno);
                                    newPost.child("Email :-").setValue(eml);
                                    newPost.child("Number :-").setValue(num);
                                    progressDialog.dismiss();






                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
