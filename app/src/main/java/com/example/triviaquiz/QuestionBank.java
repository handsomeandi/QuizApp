package com.example.triviaquiz;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

//Class that describes bank of all quiz questions
public class QuestionBank {
    //Array of all questions
    private static ArrayList<Question> question_bank = new ArrayList<Question>();
    //Url from which we get questions
    String url = "https://raw.githubusercontent.com/handsomeandi/random-stufff/master/questions_dumped_final_200.json";
    //Method that gets all the questions. It takes interface as parameter to identify that data received
    public ArrayList<Question> get_http_data_array(final AsyncGetDataForQuiz callback, Context context) {
        //Creating JsonArrayRequest to get data from the url
        JsonArrayRequest quiz = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Parsing JsonArray into the array of Question objects
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                question_bank.add(new Question(response.getJSONArray(i).get(0).toString(), response.getJSONArray(i).get(1).toString(), null, false, false));
                            }
                        } catch (Exception e) {
//                            question_bank = null;
                            Log.d(TAG, "onResponse: " + e.toString());
                        }
                        //Checking if callback is not null to identify if data receiving finished
                        if(null != callback){
                            //Calling method to signal that data is received
                            callback.onProcessFinished(question_bank);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                question_bank = null;
                Log.d(TAG, "onResponse: " + error.toString());
            }
        });
//        if(callback != null){
//            callback.processFinished(question_bank);
//        }
        //Adding request to RequestQueue
        SingletonRequest.getInstance().addToRequestQueue(quiz,context);
        return question_bank;
    }


}
