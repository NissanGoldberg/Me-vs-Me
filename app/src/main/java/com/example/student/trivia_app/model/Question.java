package com.example.student.trivia_app.model;

/**
 * Created by Student on 06/12/2018.
 */

public class Question {

    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String level;
    String question;
    String rightAnswer;

    public Question() {
        answer1 = "";
        answer2 = "";
        answer3 = "";
        answer4 = "";
        level = "";
        question = "";
        rightAnswer = "";
    }

    public Question(Question new_question){
        this.answer1     =   new_question.answer1;
        this.answer2     =   new_question.answer2;
        this.answer3     =   new_question.answer3;
        this.answer4     =   new_question.answer4;
        this.level       =   new_question.level;
        this.question    =   new_question.question;
        this.rightAnswer =   new_question.rightAnswer;
    }

    public Question(String answer1, String answer2, String answer3, String answer4, String level, String question, String rightAnswer) {
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.level = level;
        this.question = question;
        this.rightAnswer = rightAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
