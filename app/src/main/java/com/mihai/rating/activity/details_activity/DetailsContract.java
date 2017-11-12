package com.mihai.rating.activity.details_activity;


import android.content.Context;

import java.util.List;

interface DetailsContract {

    interface View {
        Context getContext();
    }

    interface Presenter {

        int getTotalWins();

        int getTotalLosses();

        List<String> getGroupHeaders();
    }
}
