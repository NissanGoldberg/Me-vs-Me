package com.example.student.trivia_app.activities;

import com.example.student.trivia_app.model.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    final String[] correctAnswer = new String[1];
    final String[] id = new String[1];
    final String[] user_score = new String[1];
    String user_email;
    FirebaseAuth mAuth;
    final int[] level = new int[1];
    float value[] = new float[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
        Intent intent = getIntent();
        id[0] = intent.getStringExtra("question_id");
        final TextView progressbar_txt = findViewById(R.id.progressBar_txt);
        progressbar_txt.setText("20 secs");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user_email = mAuth.getCurrentUser().getEmail().replace(".", "|");

        Toast.makeText(getApplicationContext(),"question "+id[0] , Toast.LENGTH_SHORT).show();

        radioGroup = findViewById(R.id.radioGroup);
        final RadioButton radioButton1 = findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = findViewById(R.id.radioButton4);
        final TextView question_tv = findViewById(R.id.question_tv);
        progressBar = findViewById(R.id.progressBar);
        long start_time = System.currentTimeMillis();
        //Radio Buttons
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef =mDatabase.getReference("/questions");
        DatabaseReference dbRef =mDatabase.getReference();
//        DatabaseReference dbRef =mDatabase.getReference("/questions" + id);

        //Read from database
        //constantly checks for changes
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_score[0] = dataSnapshot.child("users/"+user_email).child("score").getValue().toString();
                dataSnapshot = dataSnapshot.child("questions").child(id[0].toString());
                level[0]=Integer.parseInt(dataSnapshot.child("level").getValue(String.class));
//                for (DataSnapshot ds : dataSnapshot.getChildren()){
                correctAnswer[0] = dataSnapshot.child("rightAnswer").getValue().toString();
                radioButton1.setText(dataSnapshot.child("answer1").getValue().toString());
                radioButton2.setText(dataSnapshot.child("answer2").getValue().toString());
                radioButton3.setText(dataSnapshot.child("answer3").getValue().toString());
                radioButton4.setText(dataSnapshot.child("answer4").getValue().toString());
                question_tv.setText(dataSnapshot.child("question").getValue().toString());

//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        progressBar.setProgress(75);

        Animation anim = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float from=0f,to=100f;
                TextView progressBar_txt = findViewById(R.id.progressBar_txt);

                super.applyTransformation(interpolatedTime, t);
                value[0] = from +(to -from )*interpolatedTime;
                progressBar.setProgress((int)value[0]);
                progressBar_txt.setText(String.format("%.1f seconds", (100-value[0])/3.33));
            }
        };
        anim.setInterpolator(new LinearInterpolator()); // do not alter animation rate

        anim.setDuration(30000);
        progressBar.setAnimation(anim);
        progressBar.setMax(100);

    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.stats:
                startActivity(new Intent(GameActivity.this, StatisticsActivity.class));
                finish();
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GameActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.about_app:
                Toast.makeText(getApplicationContext(),"Created by Aviv, Nissan & Moshe",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submit_answer(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
//        DatabaseReference dbRef_users =mDatabase.getReference("/users");



        if (correctAnswer[0].equals(radioButton.getText())){
            progressBar.clearAnimation();
            final int game_score = (int)(100- value[0]);
//            Toast.makeText(getApplicationContext(),(100 - value[0])+"",Toast.LENGTH_SHORT).show();

//            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference dbRef =mDatabase.getReference("/users/"+user_email);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user= dataSnapshot.getValue(User.class);

                    if(user.getQuestions_answered()==null || !user.getQuestions_answered().containsKey("question "+id[0])) {

                        int addscore=(game_score*level[0]);
                        Toast.makeText(getApplicationContext(),"Added "+addscore+" score!",Toast.LENGTH_SHORT).show();

                        dbRef.child("questions_answered").child("question " + id[0]).setValue(addscore);
                        final int score = Integer.parseInt(user_score[0]) +addscore;
                        dbRef.child("score").setValue(score);
//                        Toast.makeText(getApplicationContext(),"Added======to DB!",Toast.LENGTH_SHORT).show();

                    }else {
//                        Toast.makeText(getApplicationContext(), "not Added to DB!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        else
            Toast.makeText(getApplicationContext(),"Loser!",Toast.LENGTH_SHORT).show();

    }

}