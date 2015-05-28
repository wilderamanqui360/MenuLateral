package com.moydev.cibertecproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moydev.cibertecproject.R;
import com.moydev.cibertecproject.db.Matches;

import java.util.List;

/**
 * Created by ATTAKON on 5/23/15.
 */
public class MatchesCustomAdapter extends RecyclerView.Adapter<MatchesCustomAdapter.MatchViewHolder> {

    List<Matches> matches;

    public MatchesCustomAdapter(List<Matches> matches) {
        this.matches = matches;
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_matches_list, viewGroup, false);
        MatchViewHolder mvh = new MatchViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MatchViewHolder matchViewHolder, int i) {
        matchViewHolder.txtMatchVs.setText(matches.get(i).getLocal() + " vs " + matches.get(i).getVisitor());
        matchViewHolder.txtMatchDate.setText(matches.get(i).getFecha() + " - " + matches.get(i).getHora());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView txtMatchVs;
        TextView txtMatchDate;

        MatchViewHolder(View itemView) {
            super(itemView);
            txtMatchVs = (TextView) itemView.findViewById(R.id.txtMatchVs);
            txtMatchDate = (TextView) itemView.findViewById(R.id.txtMatchDate);

        }
    }

}
