package com.example.worthit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Trainee_Registration extends AppCompatActivity {

    //Create object of DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://worth-it-af3f1-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_registration);

        final EditText fullname = findViewById(R.id.name);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText conpassword = findViewById(R.id.conpassword);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get data from EditTexts into String variables
                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String comPasswordTxt = conpassword.getText().toString();

                //check if user fill all the fields before sending data to firebase
                if(fullnameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Trainee_Registration.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                //check if password are matching with each other
                //if not matching with each then show a toast message
                else if(!passwordTxt.equals(comPasswordTxt)){
                    Toast.makeText(Trainee_Registration.this, "Passowrds are not matching", Toast.LENGTH_SHORT).show();
                }

                else{

                    databaseReference.child("trainee").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if phone is not registered before

                            if(snapshot.hasChild(phoneTxt)){
                                Toast.makeText(Trainee_Registration.this, "Phone is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //sending data to firebase Realtime Database
                                //using Phone number as unique identifier
                                //so all details of every user comes under phone number
                                databaseReference.child("trainee").child(phoneTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("trainee").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("trainee").child(phoneTxt).child("password").setValue(passwordTxt);

                                //show a success message then finish the activity
                                Toast.makeText(Trainee_Registration.this, "Trainee registered seccessfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}