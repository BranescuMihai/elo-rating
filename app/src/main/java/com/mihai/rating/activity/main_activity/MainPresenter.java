package com.mihai.rating.activity.main_activity;

import com.mihai.rating.elo_algorithm.EloRankingManager;
import com.mihai.rating.elo_algorithm.EloRankingManagerFacade;
import com.mihai.rating.model.Player;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class MainPresenter implements MainContract.Presenter {

    private EloRankingManagerFacade eloRankingManager;
    private MainContract.View view;

    MainPresenter(MainContract.View view) {
        this.view = view;
        eloRankingManager = new EloRankingManager(view.getContext());
        startLoadingPlayers();
    }

    private void startLoadingPlayers() {
        if (view == null) {
            return;
        }

        view.startProgress();

        Observable<List<Player>> playersObservable = eloRankingManager.getPlayersObservable();
        playersObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<List<Player>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.stopProgress();
                            }

                            @Override
                            public void onNext(List<Player> players) {
                                view.dataWasLoaded(players);
                                view.stopProgress();
                            }
                        });
    }

    @Override
    public Player getDetailedPlayer(int position) {
        return eloRankingManager.getDetailedPlayer(position);
    }
}
