package com.example.student.trivia_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSubject extends AppCompatActivity {
    EditText subject_name_txt;
    EditText url_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        subject_name_txt = findViewById(R.id.editText_subject_name);
        url_txt = findViewById(R.id.editText_subject_icon_url);
    }

    public void add_subject(View view){
        String sub_name = subject_name_txt.getText().toString();
        String url_str = url_txt.getText().toString();

        //check if url is valid and contains image for icon
        if(!URLUtil.isValidUrl(url_str) || !check_url_image(url_str) ){
            Toast.makeText(getApplicationContext(),"The url is invalid or has no image",Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase mDatabase =FirebaseDatabase.getInstance();
        DatabaseReference dbRootRef =mDatabase.getReference("/subjects");
        dbRootRef.child(sub_name).setValue(sub_name);
        dbRootRef.child(sub_name).child("imageURL").setValue(url_str);
        Toast.makeText(getApplicationContext(),"added subject: "+ sub_name,Toast.LENGTH_SHORT).show();
//        finish();
    }

    private boolean check_url_image(String str){
        if (str.endsWith(".png") ||
                str.endsWith(".gif") ||
                str.endsWith(".jpeg") ||
                str.endsWith(".jpg"))
            return true;
        return false;
    }
}
