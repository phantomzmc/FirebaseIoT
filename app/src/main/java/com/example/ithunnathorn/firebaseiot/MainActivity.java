package com.example.ithunnathorn.firebaseiot;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputName;
    private EditText inputEmail;
    private Button btnSave;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        
        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName =(EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        btnSave = (Button) findViewById(R.id.btn_save);
        
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("user");
        
        mFirebaseInstance.getReference("app_title").setValue("Thunathorn Firebase IoT");
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "app title update");
                
                String apptitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(apptitle);
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"ผิดพลาด", databaseError.toException());

            }
        });
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                if (TextUtils.isEmpty(userId)){
                   createUser(name,email);
                }else {
                    createUser(name,email);
                }
                
                
            }

        });
        toggleButton();
        
    }

    private void toggleButton() {
        if (TextUtils.isEmpty(userId)){
            btnSave.setText("บันทึกข้อมูล");
        }
        else {
            btnSave.setText("เเก้ไขข้อมูล");
        }
    }


    private void createUser(String name, String email) {
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
        User user = new User(name, email);
        mFirebaseDatabase.child(userId).setValue(user);
        addUserChangeListener();
    }

    private void addUserChangeListener() {
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null){
                    Log.e(TAG,"user data is null");
                    return;
                }
                Log.e(TAG,"User data change" + user.name +", " + user.email);
                //txtDetails.setText(user.name +"," + user.email);
                inputName.setText("'");
                inputEmail.setText("");

                toggleButton();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG,"failed to red user error",databaseError.toException());

            }
        });

    }
    private void UpdateUser(String name, String email){
        if (!TextUtils.isEmpty(name)){
            mFirebaseDatabase.child(userId).child("name").setValue(name);
        }
        if (!TextUtils.isEmpty(email)){
            mFirebaseDatabase.child(userId).child("email").setValue(email);
        }
    }


}
