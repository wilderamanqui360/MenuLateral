package com.moydev.cibertecproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moydev.cibertecproject.PlayersFragment;
import com.moydev.cibertecproject.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ATTAKON on 4/25/15.
 */
public class PlayersCustomAdapter extends RecyclerView.Adapter<PlayersCustomAdapter.ViewHolder> {

    public JSONArray content;
    Context context;
    PlayersFragment.OnPlayerSelected listener;

    public PlayersCustomAdapter(Context context, JSONArray response, PlayersFragment.OnPlayerSelected listener){
        content = response;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_list, parent, false);
        ViewHolder view_holder = new ViewHolder(view);
        return view_holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //Insertamos la texto obtenido desde el sistema web
        String urlImg = "";
        try {
            String playerFName = content.getJSONObject(i).getString("firstName");
            String playerLName = content.getJSONObject(i).getString("lastName");
            String position = content.getJSONObject(i).getString("position");

            viewHolder.player_name.setText(playerFName + " " + playerLName);
            viewHolder.player_position.setText(position);
            urlImg = content.getJSONObject(i).getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(context).load(urlImg).into(viewHolder.player_img);

    }

    @Override
    public int getItemCount() {
        return 23;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView player_name;
        TextView player_position;
        ImageView player_img;

        public ViewHolder(View view) {
            super(view);
            player_name = (TextView) view.findViewById(R.id.player_name);
            player_position = (TextView) view.findViewById(R.id.player_position);
            player_img = (ImageView) view.findViewById(R.id.player_img);
        }

        @Override
        public void onClick(View v) {
            try {
                listener.OnPlayerSelected(content.getJSONObject(getPosition()).getInt("age"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
