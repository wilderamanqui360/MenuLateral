package com.moydev.cibertecproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.moydev.cibertecproject.adapter.PlayersCustomAdapter;
import com.moydev.cibertecproject.volley.VolleySingleton;
import com.shamanland.fab.FloatingActionButton;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {

    private OnPlayerSelected listener;
    RecyclerView player_list;
    LinearLayoutManager layout_manager;
    PlayersCustomAdapter content_adapter;
    private VolleySingleton volley_client;
    protected RequestQueue request_queue;
    FloatingActionButton fab;

    public PlayersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V =inflater.inflate(R.layout.fragment_players, container, false);
        player_list = (RecyclerView) V.findViewById(R.id.player_list);
        player_list.setHasFixedSize(true);
        layout_manager = new LinearLayoutManager(getActivity());
        player_list.setLayoutManager(layout_manager);
        volley_client = VolleySingleton.getInstance(getActivity().getApplicationContext());
        request_queue = volley_client.getRequest_queue();
        fab = (FloatingActionButton) V.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMatchListFragment();
            }
        });
        return V;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnPlayerSelected) activity;
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

    public interface OnPlayerSelected {
        // TODO: Update argument type and name
        public void OnPlayerSelected(Integer teamId);
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

    public void makeRequest(Integer teamId){
        String url = "http://moymdev.appspot.com/players?teamId="+teamId;
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                content_adapter = new PlayersCustomAdapter(getActivity(), response, listener);
                player_list.setAdapter(content_adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        addToQueue(request);
    }

    public void goMatchListFragment(){
        MatchListFragment matchListFragment= new MatchListFragment();
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.players_fragment, matchListFragment, null)
                .addToBackStack(null)
                .commit();
    }


}
