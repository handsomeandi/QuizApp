package com.example.triviaquiz;

import java.util.ArrayList;

//Interface that plays role of callback
public interface AsyncGetDataForQuiz {
    //Method that signals that process completed and data received
    void onProcessFinished(ArrayList<Question> questions);
}
