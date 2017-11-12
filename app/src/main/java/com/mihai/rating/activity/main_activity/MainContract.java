package com.mihai.rating.activity.main_activity;

import android.content.Context;

import com.mihai.rating.model.Player;

import java.util.List;


interface MainContract {

    interface View {

        void startProgress();

        void stopProgress();

        void dataWasLoaded(List<Player> players);

        Context getContext();
    }

    interface Presenter {

        Player getDetailedPlayer(int position);
    }
}
