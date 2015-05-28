package com.moydev.cibertecproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moydev.cibertecproject.TeamFragment;
import com.squareup.picasso.Picasso;

import com.moydev.cibertecproject.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ATTAKON on 4/24/15.
 */
public class TeamCustomAdapter extends RecyclerView.Adapter<TeamCustomAdapter.ViewHolder> {

    public JSONObject content;
    Context context;
    TeamFragment.OnTeamSelected listener;

    public TeamCustomAdapter(Context context, JSONObject response, TeamFragment.OnTeamSelected listener){
        content = response;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_list, parent, false);

        ViewHolder view_holder = new ViewHolder(view);

        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //Insertamos la texto obtenido desde el sistema web
        String urlImg = "";
        try {
            viewHolder.team_name.setText(content.getJSONArray("data").getJSONObject(i).getString("c_Team_es"));
            urlImg = content.getJSONArray("data").getJSONObject(i).getString("c_LogoImage");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(context).load(urlImg).into(viewHolder.team_flag);
    }

    @Override
    public int getItemCount() {
        try {
            return content.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView team_name;
        ImageView team_flag;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            team_name = (TextView) view.findViewById(R.id.team_name_item);
            team_flag = (ImageView) view.findViewById(R.id.team_flag_item);
            card_view = (CardView) view.findViewById(R.id.team_item_card);

            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                listener.OnTeamSelected(content.getJSONArray("data").getJSONObject(getPosition()).getInt("n_PotPosition"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
