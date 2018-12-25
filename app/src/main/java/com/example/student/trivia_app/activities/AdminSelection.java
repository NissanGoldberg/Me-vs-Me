package com.example.student.trivia_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.student.trivia_app.R;

public class AdminSelection extends AppCompatActivity {
    //TODO select either to add subject/question or edit prev one
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(),"Welcome Admin",Toast.LENGTH_LONG).show();
    }

    public void add_edit_question(View view){
        startActivity(new Intent(AdminSelection.this, CategoriesActivity.class)
                .putExtra("permissions","true"));
    }

    public void add_subject(View view){
        startActivity(new Intent(AdminSelection.this, AddSubject.class));    }
}
