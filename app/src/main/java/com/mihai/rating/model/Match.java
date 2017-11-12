package com.mihai.rating.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {

    private String firstPlayerId;
    private String secondPlayerId;
    private MatchState result;

    public Match(String firstPlayerId, String secondPlayerId, MatchState result) {
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
        this.result = result;
    }

    public String getFirstPlayerId() {
        return firstPlayerId;
    }

    public String getSecondPlayerId() {
        return secondPlayerId;
    }

    public MatchState getResult() {
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstPlayerId);
        dest.writeString(this.secondPlayerId);
        dest.writeInt(this.result == null ? -1 : this.result.ordinal());
    }

    private Match(Parcel in) {
        this.firstPlayerId = in.readString();
        this.secondPlayerId = in.readString();
        int tmpResult = in.readInt();
        this.result = tmpResult == -1 ? null : MatchState.values()[tmpResult];
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel source) {
            return new Match(source);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}
