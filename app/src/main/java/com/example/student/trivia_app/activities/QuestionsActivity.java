package com.example.student.trivia_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    final String[] id = new String[1];
    String permissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        permissions = intent.getStringExtra("permissions");

        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //print the catergory id
        id[0] = intent.getStringExtra("category_item");
//        user_hash = intent.getStringExtra("user_hash");
        final String[] new_question_id = new String[1];
        Toast.makeText(getApplicationContext(),id[0] , Toast.LENGTH_SHORT).show();

        final LinearLayout question_gallery = findViewById(R.id.gallery_questions);
        if(question_gallery.getChildCount() != 0){
            question_gallery.removeAllViews();
        }
        final LayoutInflater inflater = LayoutInflater.from(this);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRefQuestions =mDatabase.getReference("/questions");
//        DatabaseReference dbRefSubject = mDatabase.getReference("/subjects/" + id[0] + "/questions");
        DatabaseReference dbRefSubject = mDatabase.getReference("/subjects/" + id[0]);

        final Button add_question_btn = new Button(QuestionsActivity.this);

        dbRefSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                if(dataSnapshot.child("questions").exists()) {
                    for (DataSnapshot ds : dataSnapshot.child("questions").getChildren()) {
                        View view = inflater.inflate(R.layout.question_item, question_gallery, false);

                        TextView question_details_tv = view.findViewById(R.id.question_details);
                        TextView question_level_tv = view.findViewById(R.id.question_level);

                        if (permissions.equals("true")) {
                            question_details_tv.setText(ds.getValue().toString());
                        } else {
                            question_level_tv.setText("");
                        }

                        question_details_tv.setText("Question: " + i++);
                        question_details_tv.setTag(ds.getKey().toString());

                        view.setTag(ds.getKey().toString());
                        question_gallery.addView(view);
                        if (permissions.equals("true") && i == (dataSnapshot.child("questions").getChildrenCount()+1)) {

                            //TODO
                            String numOfQuestion = ds.getKey().toString().replaceAll("\\D+","");
                            String subjectName = ds.getKey().toString().replaceAll("_\\d+","");
                            new_question_id[0] = subjectName + "_" + (Integer.parseInt(numOfQuestion) + 1);
                            add_question_btn.setText("Add question");
                            add_question_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(QuestionsActivity.this, AddQuestion.class)
                                            .putExtra("new_question_id", new_question_id[0])
                                            .putExtra("category_item", id[0]));
                                    finish();
                                }
                            });


                            if (add_question_btn.getParent() == null) {
                        /*if(dataSnapshot.child("")==null){*/

                                question_gallery.addView(add_question_btn);
                            }
                        }

                    }
                }
                else {

                    if (permissions.equals("true")) {

                        String subjectName = dataSnapshot.getKey().toString();
                        new_question_id[0] = subjectName + "1";
                        add_question_btn.setText("Add question");
                        add_question_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(QuestionsActivity.this, AddQuestion.class)
                                        .putExtra("new_question_id", new_question_id[0])
                                        .putExtra("category_item", id[0]));
                                finish();
                            }
                        });


                        if (add_question_btn.getParent() == null) {
                        /*if(dataSnapshot.child("")==null){*/

                            question_gallery.addView(add_question_btn);
                        }
                    }
                    //add button for admins that allow to add question
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.stats:
                startActivity(new Intent(QuestionsActivity.this, StatisticsActivity.class));
                finish();
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.about_app:
                Toast.makeText(getApplicationContext(),"Created by Aviv, Nissan & Moshe",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void questionSelect(View view){
//        Toast.makeText(getApplicationContext(),"hello" , Toast.LENGTH_SHORT).show();
        //TODO setTag doesnt work always null
        String question_id = view.getTag().toString();
        Toast.makeText(getApplicationContext(),question_id , Toast.LENGTH_SHORT).show();
//        System.out.println(question_id);
        if(permissions.equals("true")){
            Intent intent = new Intent(QuestionsActivity.this, AddQuestion.class);
            //will send the category_item to other activity
            intent.putExtra("old_question_id", question_id);
            intent.putExtra("category_item",id[0]);
//
            startActivity(intent);
        }else {
            Intent intent = new Intent(QuestionsActivity.this, GameActivity.class);
            //will send the category_item to other activity
            intent.putExtra("question_id", question_id);
//        intent.putExtra("user_hash",user_hash);
//
            startActivity(intent);
        }
    }

}