package com.example.student.trivia_app.model;

import android.util.SparseArray;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Student on 06/12/2018.
 */

public class Category {
    Map<Integer,String> question_id = new HashMap<>();


    public Category() {
    }

    public Category(Map<Integer, String> question_id) {
        this.question_id = question_id;
    }

    public Map<Integer, String> getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Map<Integer, String> question_id) {
        this.question_id = question_id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question_id", question_id);
        return result;
    }
}
