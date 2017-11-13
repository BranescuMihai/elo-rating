package com.mihai.rating.utils;

import android.content.Context;
import android.util.Log;

import com.mihai.rating.R;
import com.mihai.rating.model.Match;
import com.mihai.rating.model.MatchState;
import com.mihai.rating.model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class FileUtils {

    private static final String TAG = FileUtils.class.getName();

    public static void readFromFile(ReadFileActions actions, Context context, int rawFile, int fileName) {

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
                    actions.addPlayer(player);
                } else {
                    Match match = new Match(split[0], split[1], MatchState.PLAYER_ONE_WON);
                    actions.setUpdatedPlayers(match);
                }
            }

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, "Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            Log.d(TAG, "Error reading file '" + fileName + "'");
        }
    }
}
