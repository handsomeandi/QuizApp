package com.example.triviaquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//MainActivity implements onClickListener to handle clicks
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining views, counters and timer for delays
    private CardView questionCard;
    private Button btnTrue, btnFalse;
    private TextView question, counter, counterQuiz, bestScore;
    private ImageView arrowBack, arrowForward;
    private ArrayList<Question> question_list;
    private int counter_num = 0;
    private int answered_correctly = 0;
    int best_score_num = 0;
    private Timer timer;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing views
        init_views();
//        loadData();

        prefs = new Prefs(MainActivity.this);



        //Creating ArrayList with all the questions by getting response from Question Bank Json Array and then implementing method of created interface that contains
        //onProcessFinished method that signals that data received
        question_list = new QuestionBank().get_http_data_array(new AsyncGetDataForQuiz() {
            @Override
            public void onProcessFinished(ArrayList<Question> questions) {
                best_score_num = prefs.get_best_score();
                counter_num = prefs.get_counter_num();
                answered_correctly = prefs.get_current_correct();
                question.setText(questions.get(counter_num).getQuestion_body());
                String txt_for_counter = counter_num+1+"/"+questions.size();
                counter.setText(txt_for_counter);
                String txt_for_best = "Best score: " + best_score_num;
                bestScore.setText(txt_for_best);
                String txt_for_currently_correct = "Correct: " + answered_correctly;
                counterQuiz.setText(txt_for_currently_correct);
            }
        }, this);

        //Setting onClickListeners for buttons as this, because we implement them by overriding method of OnCLickListener Interface
        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);
        arrowBack.setOnClickListener(this);
        arrowForward.setOnClickListener(this);
    }


    private void save_data(){
        prefs.set_current_correct(answered_correctly);
        prefs.set_counter_num(counter_num);
        prefs.set_best_score(best_score_num);
        prefs.set_current_answered(counter_num, question_list.get(counter_num).isIs_answered());
    }

    @Override
    protected void onPause() {
        save_data();
        super.onPause();
    }

    //Method that handles click on "True" or "False" button
    private void handle_answer_click(String answer){
        //Creating and assigning Animation variable that shakes answer if wrong
        Animation shake_card = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        //Animators that changes color of CardView after answer is clicked
        ValueAnimator anim_red = ValueAnimator.ofArgb(getResources().getColor(R.color.red), getResources().getColor(R.color.white));
        anim_red.setDuration(2000);
        anim_red.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                questionCard.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator anim_green = ValueAnimator.ofArgb(getResources().getColor(R.color.green), getResources().getColor(R.color.white));
        anim_green.setDuration(2000);
        anim_green.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                questionCard.setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
            }
        });
        //Setting buttons disabled
        btnFalse.setEnabled(false);
        btnTrue.setEnabled(false);
        //Checking if answer is correct or not and starting corresponding animations
            if (!question_list.get(counter_num).isIs_answered()) {
                question_list.get(counter_num).setUser_answer(answer);
                question_list.get(counter_num).setIs_answered(true);
                prefs.set_current_answered(counter_num,true);
                if (question_list.get(counter_num).getUser_answer().equals(question_list.get(counter_num).getAnswer())) {
                    question_list.get(counter_num).setAnswered_correctly(true);
                    anim_green.start();
                    Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    answered_correctly += 100;
                    if(answered_correctly > best_score_num){
                        best_score_num = answered_correctly;
                        prefs.set_best_score(best_score_num);
                    }
                } else {
                    question_list.get(counter_num).setAnswered_correctly(false);
                    questionCard.startAnimation(shake_card);
                    anim_red.start();
                    Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    answered_correctly = 0;
                }
                //Switching to next question
                prefs.set_current_correct(answered_correctly);
                handle_next_question_with_delay();
            }
//        }
    }

    //Switching to next question after answer clicked
    private void handle_next_question_with_delay(){
        if(counter_num<question_list.size()-1) {
            counter_num++;
//            saveData();
//            update_views(question_list);
            prefs.set_counter_num(counter_num);
            set_question_text_after_delay();
//            check_is_answered();
        }

    }

    //Switching to next question after arrow_forward clicked
    private void handle_next_question(){
        if(counter_num<question_list.size()-1) {
            counter_num++;
            set_question_text();
            check_is_answered();
        }
    }

    //Switching to previous question after arrow_back clicked
    private void handle_previous_question(){
        questionCard.setBackgroundColor(getResources().getColor(R.color.white));
        if(counter_num>0) {
            counter_num--;
            set_question_text();
            check_is_answered();
        }
    }

    //Setting question and counter texts
    private void set_question_text(){
        question.setText(question_list.get(counter_num).getQuestion_body());
        String txt_for_counter = counter_num+1+"/"+question_list.size();
        counter.setText(txt_for_counter);
        String txt_for_best = "Best score: " + best_score_num;
        bestScore.setText(txt_for_best);
        String txt_for_currently_correct = "Correct: " + answered_correctly;
        counterQuiz.setText(txt_for_currently_correct);
    }

    //Setting question and counter texts after answer clicked
    private void set_question_text_after_delay(){
        String txt_for_counter = counter_num+1+"/"+question_list.size();
        arrowBack.setEnabled(false);
        arrowForward.setEnabled(false);
        question.postDelayed(new Runnable() {
            @Override
            public void run() {
                arrowBack.setEnabled(true);
                arrowForward.setEnabled(true);
//                question.setText(question_list.get(counter_num).getQuestion_body());
//                counter.setText(txt_for_counter);
                question.setText(question_list.get(counter_num).getQuestion_body());
                String txt_for_counter = counter_num+1+"/"+question_list.size();
                counter.setText(txt_for_counter);
                String txt_for_best = "Best score: " + best_score_num;
                bestScore.setText(txt_for_best);
                String txt_for_currently_correct = "Correct: " + answered_correctly;
                counterQuiz.setText(txt_for_currently_correct);
                check_is_answered();
            }
        }, 1500);
    }


    //Checking if question is already answered
    private void check_is_answered(){
//        SharedPreferences getSharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean isAnsw = prefs.get_current_answered(counter_num);
//        boolean isAnsw = getSharedPrefs.getBoolean("quest_object"+counter_num, false);
        if(isAnsw){
            btnFalse.setEnabled(false);
            btnTrue.setEnabled(false);
        }else{
            btnFalse.setEnabled(true);
            btnTrue.setEnabled(true);
        }
    }


    //Initializing views
    private void init_views(){
        questionCard = findViewById(R.id.question_card);
        counterQuiz = findViewById(R.id.correct_answers);
        arrowBack = findViewById(R.id.arrow_back);
        arrowForward = findViewById(R.id.arrow_forward);
        question = findViewById(R.id.question);
        bestScore = findViewById(R.id.bestScore);
        counter = findViewById(R.id.counter_quiz);
        btnTrue = findViewById(R.id.btn_true);
        btnFalse = findViewById(R.id.btn_false);
        timer = new Timer();
    }


    //Implementing onClickListeners
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_true:
                handle_answer_click("true");
                break;
            case R.id.btn_false:
                handle_answer_click("false");
                break;
            case R.id.arrow_forward:
                handle_next_question();
                break;
            case R.id.arrow_back:
                handle_previous_question();
                break;
        }
    }
}