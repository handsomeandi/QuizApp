package com.example.triviaquiz;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

//Class that creates Singleton of Json Request and impements Singleton Pattern
public class SingletonRequest extends Application {
    private static SingletonRequest myRequest;
    private RequestQueue requestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        myRequest = this;
    }

    public static synchronized SingletonRequest getInstance(){
        if(myRequest == null){
            myRequest = new SingletonRequest();
        }
        return myRequest;
    }

    //Method that allows to get RequestQueue
    public RequestQueue getRequestQueue(Context context){
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    //Method that allows to add Request to RequestQueue
    public void addToRequestQueue(JsonArrayRequest arrayRequest, Context context){
        getRequestQueue(context).add(arrayRequest);
    }

    private SingletonRequest(){
    }
}
