package com.example.student.trivia_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.model.Category;
import com.example.student.trivia_app.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference dbRootRef;
    private int REQUEST_CODE;
    Button sign_in_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase
        mDatabase = FirebaseDatabase.getInstance();
        dbRootRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        sign_in_btn = findViewById(R.id.sign_in_btn);


        if (mAuth.getCurrentUser() != null)
        {
            DatabaseReference dbRef =mDatabase.getReference("users");
            //TODO close the value event listener
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //go through all users and send to next activity the hash of the user

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        User user = ds.getValue(User.class);
                        String user_email = mAuth.getCurrentUser().getEmail().replace(".", "|");
                        if (ds.getKey().equals(user_email) && user.isPermissions()==true){
                            startActivity(new Intent(MainActivity.this, AdminSelection.class).putExtra("permissions","true"));
                            finish();
                        }else if (ds.getKey().equals(user_email) && user.isPermissions()==false){
                            startActivity(new Intent(MainActivity.this, CategoriesActivity.class).putExtra("permissions","false"));
                            finish();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            System.out.println("===================");
//            System.out.println(mAuth.getCurrentUser().getEmail());

        }
        else //not signed in
        {
            //send button view
            authenticateUser(sign_in_btn);
        }
    }


    public void authenticateUser(View v)
    {
        System.out.println("============entered foo================");
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), REQUEST_CODE);
    }

    public void sign_out(View v)
    {
        System.out.println("============entered foo================");
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(),"signed out",Toast.LENGTH_SHORT).show();
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //FIXME add to user table
                //FIXME Check if in db if so startActivity right away
                DatabaseReference dbRootRef_temp = mDatabase.getReference();


                String user_email = mAuth.getCurrentUser().getEmail().replace(".", "|");

                //Check if user already exists
                dbRootRef.child("users").addValueEventListener(new ValueEventListener() {
                    String usr_email = mAuth.getCurrentUser().getEmail().replace(".", "|");
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        if(ds.child(usr_email).exists()){
                            if(ds.child(usr_email).child("permissions").equals("true")){ //Exists user and is admin
                                startActivity(new Intent(MainActivity.this, CategoriesActivity.class).putExtra("permissions","true"));
                                finish();
                            }else{ //There exists a user but isnt admin
                                startActivity(new Intent(MainActivity.this, CategoriesActivity.class).putExtra("permissions","false"));
                                finish();
                            }
                        }else{ //New user
                            Map<String, Object> childUpdates = new HashMap<>();
                            User user = new User(false,0);
                            childUpdates.put("/users/"+usr_email+"/" , user);
                            dbRootRef.updateChildren(childUpdates);
                            startActivity(new Intent(MainActivity.this, CategoriesActivity.class).putExtra("permissions","false"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled Sign-in
                System.out.println("===============try to bypass me aye");
                return;
            }
            if (response == null) {
                // User cancelled Sign-in
                System.out.println("===============try to bypass me aye");
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                // Device has no network connection
                System.out.println("===============try to bypass me aye");
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                // Unknown error occurred
                System.out.println("===============try to bypass me aye");
                return;
            }
        }
    }

}
