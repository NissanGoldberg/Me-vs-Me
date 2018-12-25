package com.example.student.trivia_app.model;
import  java.util.*;

/**
 * Created by Student on 13/12/2018.
 */

public class User {
    boolean permissions;
    int score;
    HashMap<String, Integer> questions_answered;


    public User() {
    }

    public User(boolean permissions, int score) {
        HashMap<String, Integer> questions_answered = new HashMap<>();

        this.permissions = permissions;
        this.score = score;
        this.questions_answered=questions_answered;
    }

    public boolean isPermissions() {
        return permissions;
    }

    public void setPermissions(boolean permissions) {
        this.permissions = permissions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public HashMap<String, Integer> getQuestions_answered() {
        return questions_answered;
    }

    public void setQuestions_answered(HashMap<String, Integer> other) {this.questions_answered = other;}


}