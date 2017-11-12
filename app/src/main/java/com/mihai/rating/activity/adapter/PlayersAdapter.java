package com.mihai.rating.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mihai.rating.R;
import com.mihai.rating.model.Player;

import java.util.List;


public class PlayersAdapter extends BaseAdapter {

    private List<Player> players;
    private PlayersListActions playersListActions;

    public PlayersAdapter(PlayersListActions playersListActions, List<Player> players) {
        this.players = players;
        this.playersListActions = playersListActions;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Player player = (Player) getItem(position);

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.player_element, parent, false);

            viewHolder.rankView = view.findViewById(R.id.player_rank);
            viewHolder.nameView = view.findViewById(R.id.player_name);
            viewHolder.containerLayout = view.findViewById(R.id.player_container);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nameView.setText(player.getName());
        String pos = position + "";
        viewHolder.rankView.setText(pos);
        viewHolder.containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playersListActions.onPlayerClicked(position);
            }
        });

        return view;
    }

    private static final class ViewHolder {
        TextView rankView;
        TextView nameView;
        LinearLayout containerLayout;
    }
}
