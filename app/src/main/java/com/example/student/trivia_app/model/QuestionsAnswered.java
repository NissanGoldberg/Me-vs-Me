package com.example.student.trivia_app.model;

import java.util.HashMap;

/**
 * Created by Student on 20/12/2018.
 */

public class QuestionsAnswered {

    HashMap<String,Integer> questions_answered;

    public QuestionsAnswered(HashMap<String, Integer> questions_answered) {
        this.questions_answered = questions_answered;
    }

    public QuestionsAnswered() {
    }

    public HashMap<String, Integer> getQuestions_answered() {
        return questions_answered;
    }

    public void setQuestions_answered(HashMap<String, Integer> questions_answered) {
        this.questions_answered = questions_answered;
    }

}
