package com.mihai.rating.elo_algorithm;

import com.mihai.rating.model.Player;

import java.util.List;

import rx.Observable;


public interface EloRankingManagerFacade {

    Observable<List<Player>> getPlayersObservable();

    Player getDetailedPlayer(int position);
}
