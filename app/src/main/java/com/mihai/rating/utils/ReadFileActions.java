package com.mihai.rating.utils;

import com.mihai.rating.model.Match;
import com.mihai.rating.model.Player;


public interface ReadFileActions {

    void addPlayer(Player player);

    void setUpdatedPlayers(Match match);
}
