package com.mihai.rating.model;

import android.os.Parcel;
import android.os.Parcelable;


public class WinsLossesResult implements Parcelable {

    private String opponent;
    private int wins;
    private int losses;

    public WinsLossesResult(String opponent, int wins, int losses) {
        this.opponent = opponent;
        this.wins = wins;
        this.losses = losses;
    }

    public String getOpponent() {
        return opponent;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addWin() {
        wins += 1;
    }

    public void addLoss() {
        losses += 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opponent);
        dest.writeInt(this.wins);
        dest.writeInt(this.losses);
    }

    private WinsLossesResult(Parcel in) {
        this.opponent = in.readString();
        this.wins = in.readInt();
        this.losses = in.readInt();
    }

    public static final Parcelable.Creator<WinsLossesResult> CREATOR = new Parcelable.Creator<WinsLossesResult>() {
        @Override
        public WinsLossesResult createFromParcel(Parcel source) {
            return new WinsLossesResult(source);
        }

        @Override
        public WinsLossesResult[] newArray(int size) {
            return new WinsLossesResult[size];
        }
    };
}
