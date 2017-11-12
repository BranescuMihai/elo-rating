package com.mihai.rating.activity.main_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mihai.rating.R;
import com.mihai.rating.activity.adapter.PlayersAdapter;
import com.mihai.rating.activity.adapter.PlayersListActions;
import com.mihai.rating.activity.details_activity.DetailsActivity;
import com.mihai.rating.model.Player;
import com.mihai.rating.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, PlayersListActions {

    private List<Player> players;
    private FrameLayout progressOverlay;
    private PlayersAdapter playersAdapter;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        players = new ArrayList<>();
        progressOverlay = (FrameLayout) findViewById(R.id.progress_overlay);
        ListView playersList = (ListView) findViewById(R.id.players_list);

        playersAdapter = new PlayersAdapter(this, players);
        playersList.setAdapter(playersAdapter);

        presenter = new MainPresenter(this);
    }

    @Override
    public void startProgress() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgress() {
        progressOverlay.setVisibility(View.GONE);
    }

    @Override
    public void dataWasLoaded(List<Player> players) {
        this.players.addAll(players);
        playersAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onPlayerClicked(int position) {

        Player detailedPlayer = presenter.getDetailedPlayer(position);
        players.set(position, detailedPlayer);

        Intent playerDetailsIntent = new Intent(this, DetailsActivity.class);
        playerDetailsIntent.putExtra(Constants.PLAYER_EXTRA, players.get(position));
        startActivity(playerDetailsIntent);
    }
}
