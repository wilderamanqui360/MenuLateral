package com.moydev.cibertecproject;


import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moydev.cibertecproject.adapter.TeamCustomAdapter;
import com.moydev.cibertecproject.volley.VolleySingleton;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    private OnTeamSelected listener;
    RecyclerView team_list;
    LinearLayoutManager layout_manager;
    TeamCustomAdapter content_adapter;
    private VolleySingleton volley_client;
    protected RequestQueue request_queue;
    ProgressBar loader;


    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_team, container, false);
        team_list = (RecyclerView) V.findViewById(R.id.team_list);
        team_list.setHasFixedSize(true);
        loader = (ProgressBar) V.findViewById(R.id.team_loader);
        onStartConnection();
        layout_manager = new LinearLayoutManager(getActivity());
        team_list.setLayoutManager(layout_manager);
        volley_client = VolleySingleton.getInstance(getActivity().getApplicationContext());
        request_queue = volley_client.getRequest_queue();

        makeRequest();
        return V;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnTeamSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTeamSelected {
        // TODO: Update argument type and name
        public void OnTeamSelected(Integer teamId);
    }

    public void addToQueue(Request request){
        if(request != null){
            request.setTag(this);
            if (request_queue == null) {
                request_queue = volley_client.getRequest_queue();
            }
            request.setRetryPolicy(new DefaultRetryPolicy(6000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            onStartConnection();
            request_queue.add(request);
        }
    }

    public void makeRequest(){
        String url = "http://moymdev.appspot.com/teams";

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("MakeRequest - Success","");
                content_adapter = new TeamCustomAdapter(getActivity(), response, listener);
                team_list.setAdapter(content_adapter);
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("MakeRequest - Error",volleyError.getMessage());
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
    }

    public void onStartConnection() {
        loader.setVisibility(View.VISIBLE);
        team_list.setVisibility(View.GONE);
    }

    public void onConnectionFinished() {
        loader.setVisibility(View.GONE);
        team_list.setVisibility(View.VISIBLE);
    }

    public void onConnectionFailed(String error) {
        loader.setVisibility(View.GONE);
        team_list.setVisibility(View.GONE);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }


}
