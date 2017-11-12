package com.mihai.rating.elo_algorithm;

import android.content.Context;

import com.mihai.rating.model.Player;
import com.mihai.rating.model.WinsLossesResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EloRatingManagerTest {

    private EloRankingManager eloRankingManager;
    @Mock
    Context mockContext;

    @Before
    public void setUp() {
        eloRankingManager = new EloRankingManager(mockContext);
    }

    @Test
    public void eloRankingManager_getDetailedPlayer() {
        eloRankingManager = Mockito.spy(eloRankingManager);

        Player player = new Player(0, "Mihai");
        eloRankingManager.players.add(player);

        List<Player> suggestions = new ArrayList<>();
        Player suggestion = new Player(1, "Eric");
        suggestions.add(suggestion);

        List<WinsLossesResult> winsLossesResults = new ArrayList<>();
        WinsLossesResult winsLossesResult = new WinsLossesResult("Eric", 1, 0);
        winsLossesResults.add(winsLossesResult);

        doNothing().when(eloRankingManager).cleanOthersDetails(player);
        doReturn(suggestions).when(eloRankingManager).getSuggestions(player);
        doReturn(winsLossesResults).when(eloRankingManager).getResults(player);

        Player result = eloRankingManager.getDetailedPlayer(0);

        assertEquals(player.getName(), result.getName());

        verify(eloRankingManager).cleanOthersDetails(player);
        verify(eloRankingManager).getSuggestions(player);
        verify(eloRankingManager).getResults(player);
        assertEquals(winsLossesResults, result.getWinsLossesResults());
        assertEquals(suggestions, result.getSuggestedMatches());
    }
}
