package com.moydev.cibertecproject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moydev.cibertecproject.adapter.TeamCustomAdapter;
import com.moydev.cibertecproject.db.Cities;
import com.moydev.cibertecproject.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ATTAKON on 5/23/15.
 */
public class ServicioBackground extends IntentService {

    private VolleySingleton volley_client;
    protected RequestQueue request_queue;

    public ServicioBackground(){
        super("servicio-background");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = "http://moymdev.appspot.com/cities";
        volley_client = VolleySingleton.getInstance(getApplicationContext());
        request_queue = volley_client.getRequest_queue();

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Ciudades",":D");
                    int size = response.getJSONObject("data").getJSONArray("cities").length();
                    Log.d("Ciudades size","" + size);
                    if(size > 0){
                        Cities.deleteAll(Cities.class);
                    }
                    for(int i = 0; i<size;i++){
                        Cities c = new Cities(response.getJSONObject("data").getJSONArray("cities").getJSONObject(i).getString("name"));
                        Log.d("Ciudad :","" + c.getName());
                        c.save();
                    }
                } catch (JSONException e) {
                    Log.d("Ciudad err","" + e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("MakeRequest - Error", volleyError.getMessage());
            }
        });
        addToQueue(request);

    }

    public void addToQueue(Request request){
        if(request != null){
            request.setTag(this);
            if (request_queue == null) {
                request_queue = volley_client.getRequest_queue();
            }
            request.setRetryPolicy(new DefaultRetryPolicy(6000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request_queue.add(request);
        }
    }

}
