package com.example.triviaquiz;

public class Question {
    String question_body;
    String answer;
    String user_answer;
    boolean is_answered;
    boolean answered_correctly;

    public Question(String question_body, String answer, String user_answer, boolean is_answered, boolean answered_correctly) {
        this.question_body = question_body;
        this.answer = answer;
        this.user_answer = user_answer;
        this.is_answered = is_answered;
        this.answered_correctly = answered_correctly;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public String getQuestion_body() {
        return question_body;
    }

    public void setQuestion_body(String question_body) {
        this.question_body = question_body;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isIs_answered() {
        return is_answered;
    }

    public void setIs_answered(boolean is_answered) {
        this.is_answered = is_answered;
    }

    public boolean isAnswered_correctly() {
        return answered_correctly;
    }

    public void setAnswered_correctly(boolean answered_correctly) {
        this.answered_correctly = answered_correctly;
    }
}
