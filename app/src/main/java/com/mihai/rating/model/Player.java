package com.mihai.rating.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.mihai.rating.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Player implements Parcelable, Comparable<Player> {

    private String name;
    private int id;
    private int score;
    private List<Match> matches;
    private List<Player> suggestedMatches;
    private List<WinsLossesResult> winsLossesResults;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = Constants.STARTING_SCORE;
        matches = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public int getId() {
        return id;
    }

    public List<Player> getSuggestedMatches() {
        return suggestedMatches;
    }

    public void setSuggestedMatches(List<Player> suggestedMatches) {
        this.suggestedMatches = suggestedMatches;
    }

    public List<WinsLossesResult> getWinsLossesResults() {
        return winsLossesResults;
    }

    public void setWinsLossesResults(List<WinsLossesResult> winsLossesResults) {
        this.winsLossesResults = winsLossesResults;
    }

    @Override
    public int compareTo(@NonNull Player player) {
        if (this.getScore() > player.getScore()) {
            return -1;
        } else if (this.getScore() < player.getScore()) {
            return 1;
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.score);
        dest.writeList(this.matches);
        dest.writeTypedList(this.suggestedMatches);
        dest.writeTypedList(this.winsLossesResults);
    }

    protected Player(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.score = in.readInt();
        this.matches = new ArrayList<>();
        in.readList(this.matches, Match.class.getClassLoader());
        this.suggestedMatches = in.createTypedArrayList(Player.CREATOR);
        this.winsLossesResults = in.createTypedArrayList(WinsLossesResult.CREATOR);
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
