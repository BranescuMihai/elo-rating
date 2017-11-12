package com.mihai.rating.activity.details_activity;


import com.mihai.rating.R;
import com.mihai.rating.model.Player;
import com.mihai.rating.model.WinsLossesResult;

import java.util.ArrayList;
import java.util.List;

class DetailsPresenter implements DetailsContract.Presenter {

    private Player player;
    private DetailsContract.View view;

    DetailsPresenter(DetailsContract.View view, Player player) {
        this.view = view;
        this.player = player;
    }

    @Override
    public int getTotalWins() {
        int totalWins = 0;
        for (WinsLossesResult winLossesResult : player.getWinsLossesResults()) {
            totalWins += winLossesResult.getWins();
        }
        return totalWins;
    }

    @Override
    public int getTotalLosses() {
        int totalLosses = 0;
        for (WinsLossesResult winLossesResult : player.getWinsLossesResults()) {
            totalLosses += winLossesResult.getLosses();
        }
        return totalLosses;
    }

    @Override
    public List<String> getGroupHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add(view.getContext().getString(R.string.header_results));
        headers.add(view.getContext().getString(R.string.header_suggestions));
        return headers;
    }
}
