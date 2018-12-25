package com.example.student.trivia_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddQuestion extends AppCompatActivity {
    EditText question_txt;
    EditText answer_1_txt;
    EditText answer_2_txt;
    EditText answer_3_txt;
    EditText answer_4_txt;
    EditText level_txt;
    EditText correct_answer_txt;
    Button submit_question_btn;
    String subject;
    String last_question_id;
    FirebaseDatabase mDatabase;

    final Question[] questionProperties = new Question[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature((Window.FEATURE_NO_TITLE));

        setContentView(R.layout.activity_add_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        questionProperties[0] = new Question();
        subject = intent.getStringExtra("category_item");
        question_txt = findViewById(R.id.question_txt);
        answer_1_txt = findViewById(R.id.answer_1_txt);
        answer_2_txt = findViewById(R.id.answer_2_txt);
        answer_3_txt = findViewById(R.id.answer_3_txt);
        answer_4_txt = findViewById(R.id.answer_4_txt);
        level_txt = findViewById(R.id.level_txt);
        correct_answer_txt = findViewById(R.id.correct_answer_txt);
        submit_question_btn = findViewById(R.id.submit_question_btn);

        mDatabase = FirebaseDatabase.getInstance();

//        The admin wants to add new question
        if(intent.getStringExtra("new_question_id") != null){
            last_question_id = intent.getStringExtra("new_question_id");
        }
        else{// The admin wants to edit old question
            last_question_id = intent.getStringExtra("old_question_id");
            DatabaseReference db_question = mDatabase.getReference("/questions/" + last_question_id);

            db_question.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    questionProperties[0] = new Question(dataSnapshot.getValue(Question.class));

                    question_txt.setText(questionProperties[0].getQuestion());
                    answer_1_txt.setText(questionProperties[0].getAnswer1());
                    answer_2_txt.setText(questionProperties[0].getAnswer2());
                    answer_3_txt.setText(questionProperties[0].getAnswer3());
                    answer_4_txt.setText(questionProperties[0].getAnswer4());
                    level_txt.setText(questionProperties[0].getLevel());
                    correct_answer_txt.setText(questionProperties[0].getRightAnswer());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        Toast.makeText(getApplicationContext(),last_question_id + "" , Toast.LENGTH_SHORT).show();
    }

    public void add_question(View view){
        DatabaseReference db_subject = mDatabase.getReference("/subjects/"+subject+"/questions/"+last_question_id);
        DatabaseReference db_question = mDatabase.getReference("/questions/" + last_question_id);

        //Add to DB
        if (check_fields()){
            String s = question_txt.getText().toString();
            db_subject.setValue(s);
            //TODO add to questions collection
            //TODO return to prev activity
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("question", question_txt.getText().toString());
            childUpdates.put("answer1", answer_1_txt.getText().toString());
            childUpdates.put("answer2", answer_2_txt.getText().toString());
            childUpdates.put("answer3", answer_3_txt.getText().toString());
            childUpdates.put("answer4", answer_4_txt.getText().toString());
            childUpdates.put("rightAnswer", correct_answer_txt.getText().toString());
            childUpdates.put("level", level_txt.getText().toString());

            db_question.updateChildren(childUpdates);
            startActivity(new Intent(AddQuestion.this, CategoriesActivity.class));

        }
    }

    private boolean check_fields() {
        if (question_txt.getText().toString().matches("") ||
                answer_1_txt.getText().toString().matches("") ||
                answer_2_txt.getText().toString().matches("") ||
                answer_3_txt.getText().toString().matches("") ||
                answer_4_txt.getText().toString().matches("") ||
                level_txt.getText().toString().matches("") ||
                correct_answer_txt.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"One of the fields are empty" , Toast.LENGTH_SHORT).show();
            return false;
        }else if (correct_answer_txt.getText().toString().equals(answer_1_txt.getText().toString())||
                    correct_answer_txt.getText().toString().equals(answer_2_txt.getText().toString()) ||
                    correct_answer_txt.getText().toString().equals(answer_3_txt.getText().toString()) ||
                    correct_answer_txt.getText().toString().equals(answer_4_txt.getText().toString()))
                    return true;
        Toast.makeText(getApplicationContext(),"The correct answer text doesn't match one of the answers" , Toast.LENGTH_SHORT).show();
        return false;
    }
}



