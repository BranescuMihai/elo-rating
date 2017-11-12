package com.mihai.rating.activity.details_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mihai.rating.R;
import com.mihai.rating.activity.adapter.PlayerDetailsAdapter;
import com.mihai.rating.model.Player;
import com.mihai.rating.utils.Constants;


public class DetailsActivity extends AppCompatActivity implements DetailsContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
    }

    private void initView() {
        Player playerDetails = getIntent().getParcelableExtra(Constants.PLAYER_EXTRA);
        if (playerDetails == null) {
            return;
        }

        DetailsContract.Presenter presenter = new DetailsPresenter(this, playerDetails);

        TextView title = (TextView) findViewById(R.id.details_title);
        TextView totalWins = (TextView) findViewById(R.id.total_wins);
        TextView totalLosses = (TextView) findViewById(R.id.total_losses);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.player_details_list);

        title.setText(String.format(getResources().getString(R.string.name_with_score),
                playerDetails.getName(), playerDetails.getScore()));
        totalWins.setText(String.format(getResources().getString(R.string.total_wins), presenter.getTotalWins()));
        totalLosses.setText(String.format(getResources().getString(R.string.total_losses), presenter.getTotalLosses()));

        PlayerDetailsAdapter playerDetailsAdapter = new PlayerDetailsAdapter(presenter.getGroupHeaders(), playerDetails);
        expandableListView.setAdapter(playerDetailsAdapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
