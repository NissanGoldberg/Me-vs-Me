package com.example.student.trivia_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.model.QuestionsAnswered;
import com.example.student.trivia_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/// update
public class StatisticsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference dbRootRef;
    Button stats1;
    Button stats2;
    Button stats3;
    HashMap<Integer,int[]> questions_answered = new HashMap<Integer,int[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_statistics);
        //firebase
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRootRef = mDatabase.getReference();
        stats1 = findViewById(R.id.stats1);
        stats2 = findViewById(R.id.stats2);
        stats3 = findViewById(R.id.stats3);
        String user_email = mAuth.getCurrentUser().getEmail().replace(".", "|");



        if (mAuth.getCurrentUser() != null)
        {
            DatabaseReference dbRef =mDatabase.getReference("users/"+user_email);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //go through all users and send to next activity the hash of the user
                    int countAnswered=0;
                    if(dataSnapshot.child("questions_answered").exists()) {
                        for (DataSnapshot ds : dataSnapshot.child("questions_answered").getChildren()) {
                            int subject= (int)ds.getKey().toString().charAt(0);
                            if(questions_answered.keySet().contains(subject)){
                                int[] properties = questions_answered.get(subject);
                                properties[0]++;
                                properties[1] += Integer.parseInt(ds.getValue().toString());
                                questions_answered.put(subject, properties);

                            }
                            else {
                                int[] properties = new int[2];
                                properties[0] = 1;
                                properties[1] = Integer.parseInt(ds.getValue().toString());
                                questions_answered.put(subject, properties);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    public void onClickStats1(View view){

        Toast.makeText(getApplicationContext(),"stast111111111111",Toast.LENGTH_LONG).show();

//        Set<String> subjectCount = questions_answered.keySet();
        int count=0;

//        for (String key: subjectCount) {



//        }

    }
    public void onClickStats2(View view){

        Toast.makeText(getApplicationContext(),"stast22222222222",Toast.LENGTH_LONG).show();
    }

    public void onClickStats3(View view){

        Toast.makeText(getApplicationContext(),"stast33333333333",Toast.LENGTH_LONG).show();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.stats:
                Toast.makeText(getApplicationContext(),"Already in Stats",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StatisticsActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.about_app:
                Toast.makeText(getApplicationContext(),"Created by Aviv, Nissan & Moshe",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
