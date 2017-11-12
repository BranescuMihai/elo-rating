package com.mihai.rating.elo_algorithm;

import android.content.Context;
import android.util.Log;

import com.mihai.rating.R;
import com.mihai.rating.model.Match;
import com.mihai.rating.model.MatchState;
import com.mihai.rating.model.Player;
import com.mihai.rating.model.WinsLossesResult;
import com.mihai.rating.utils.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;


public class EloRankingManager implements EloRankingManagerFacade {

    private static final String TAG = EloRankingManager.class.getName();

    List<Player> players;
    private Observable<List<Player>> playersObservable;

    public EloRankingManager(final Context context) {
        players = new ArrayList<>();
        playersObservable = Observable.fromCallable(new Callable<List<Player>>() {
            @Override
            public List<Player> call() throws Exception {
                readFromFile(context, R.raw.names, R.string.file_names);
                readFromFile(context, R.raw.matches, R.string.file_matches);
                Collections.sort(players);
                return players;
            }
        });
    }

    @Override
    public Observable<List<Player>> getPlayersObservable() {
        return playersObservable;
    }

    @Override
    public Player getDetailedPlayer(int position) {

        Player player = players.get(position);
        if (player.getWinsLossesResults() != null) {
            return player;
        }

        cleanOthersDetails(player);

        player.setWinsLossesResults(getResults(player));
        player.setSuggestedMatches(getSuggestions(player));

        players.set(position, player);

        return player;
    }

    private void setUpdatedPlayers(Match match) {
        Player firstPlayer = players.get(Integer.valueOf(match.getFirstPlayerId()));
        Player secondPlayer = players.get(Integer.valueOf(match.getSecondPlayerId()));

        double firstPlayerTransformedRating = Math.pow(10, firstPlayer.getScore() / Constants.ELO_DIVIDER);
        double secondPlayerTransformedRating = Math.pow(10, secondPlayer.getScore() / Constants.ELO_DIVIDER);

        double firstPlayerExpectedScore = firstPlayerTransformedRating
                / (firstPlayerTransformedRating + secondPlayerTransformedRating);
        double secondPlayerExpectedScore = secondPlayerTransformedRating
                / (firstPlayerTransformedRating + secondPlayerTransformedRating);

        int firstPlayerWon = 0;
        int secondPlayerWon = 0;

        if (match.getResult() == MatchState.PLAYER_ONE_WON) {
            firstPlayerWon = 1;
        } else {
            secondPlayerWon = 1;
        }

        double firstPlayerUpdatedScore = firstPlayer.getScore()
                + Constants.K_FACTOR * (firstPlayerWon - firstPlayerExpectedScore);
        double secondPlayerUpdatedScore = secondPlayer.getScore()
                + Constants.K_FACTOR * (secondPlayerWon - secondPlayerExpectedScore);

        firstPlayer.setScore((int) firstPlayerUpdatedScore);
        secondPlayer.setScore((int) secondPlayerUpdatedScore);

        firstPlayer.addMatch(match);
        Match reverseMatch = new Match(match.getSecondPlayerId(), match.getFirstPlayerId(), MatchState.PLAYER_TWO_WON);
        secondPlayer.addMatch(reverseMatch);

        players.set(firstPlayer.getId(), firstPlayer);
        players.set(secondPlayer.getId(), secondPlayer);
    }

    private void readFromFile(Context context, int rawFile, int fileName) {

        boolean isReadingNames = true;

        if (fileName == R.string.file_matches) {
            isReadingNames = false;
        }

        String line;
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(context.getResources().openRawResource(rawFile));
            BufferedReader bufferedReader =
                    new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(" ");
                if (isReadingNames) {
                    Player player = new Player(Integer.valueOf(split[0]), split[1]);
                    players.add(player);
                } else {
                    Match match = new Match(split[0], split[1], MatchState.PLAYER_ONE_WON);
                    setUpdatedPlayers(match);
                }
            }

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Log.d(TAG, "Error reading file '" + fileName + "'");
        }
    }

    private int getOpponentIndex(List<WinsLossesResult> winsLossesResults, String opponent) {
        for (int i = 0; i < winsLossesResults.size(); i++) {
            WinsLossesResult winsLossesResult = winsLossesResults.get(i);
            if (winsLossesResult.getOpponent().equals(opponent)) {
                return i;
            }
        }
        return -1;
    }

    private String getOpponentName(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player.getName();
            }
        }
        return "";
    }

    List<WinsLossesResult> getResults(Player player) {

        List<WinsLossesResult> winsLossesResults = new ArrayList<>();
        for (Match match : player.getMatches()) {
            String opponent = getOpponentName(Integer.valueOf(match.getSecondPlayerId()));

            int index = getOpponentIndex(winsLossesResults, opponent);
            WinsLossesResult winsLossesResult;
            if (index != -1) {
                winsLossesResult = winsLossesResults.get(index);
            } else {
                winsLossesResult = new WinsLossesResult(opponent, 0, 0);
            }

            if (match.getResult() == MatchState.PLAYER_ONE_WON) {
                winsLossesResult.addWin();
            } else {
                winsLossesResult.addLoss();
            }

            if (index != -1) {
                winsLossesResults.set(index, winsLossesResult);
            } else {
                winsLossesResults.add(winsLossesResult);
            }
        }
        return winsLossesResults;
    }

    List<Player> getSuggestions(Player player) {
        List<Player> suggestions = new ArrayList<>();
        for (Player suggestion : players) {
            if (!suggestion.getName().equals(player.getName()) &&
                    Math.abs(suggestion.getScore() - player.getScore()) < Constants.SUGGESTION_ELO_RANGE) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    void cleanOthersDetails(Player safePlayer) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (!player.getName().equals(safePlayer.getName())) {
                player.setSuggestedMatches(null);
                player.setWinsLossesResults(null);
                players.set(i, player);
            }
        }
    }
}
