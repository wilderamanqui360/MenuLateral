package com.moydev.cibertecproject;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moydev.cibertecproject.adapter.MatchesCustomAdapter;
import com.moydev.cibertecproject.db.Matches;
import com.shamanland.fab.FloatingActionButton;

import java.util.List;


public class MatchListFragment extends Fragment {

    FloatingActionButton fab;
    RecyclerView match_list;
    LinearLayoutManager layout_manager;

    public MatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_match_list, container, false);
        fab = (FloatingActionButton) V.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goScheduleFragment();
            }
        });
        match_list = (RecyclerView)V.findViewById(R.id.match_list);
        match_list.setHasFixedSize(true);
        layout_manager = new LinearLayoutManager(getActivity());
        match_list.setLayoutManager(layout_manager);
        match_list.setItemAnimator(new DefaultItemAnimator());
        getMatchList();
        return V;
    }

    public void goScheduleFragment(){
        ScheduleFragment scheduleFragment= new ScheduleFragment();
        this.getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(getId(), scheduleFragment, null)
                .addToBackStack(null)
                .commit();
    }


    public void getMatchList() {
        Matches matches = new Matches();
        List<Matches> matchesList = matches.listAll(Matches.class);
        MatchesCustomAdapter adapter = new MatchesCustomAdapter(matchesList);
        match_list.setAdapter(adapter);
    }
}
