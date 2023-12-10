package com.example.unieval.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Criteria implements Parcelable {

    private University university;
    private int score;

    public Criteria(University university, int score) {
        this.university = university;
        this.score = score;
    }

    public static final Creator<Criteria> CREATOR = new Creator<Criteria>() {
        @Override
        public Criteria createFromParcel(Parcel in) {
            return new Criteria(in);
        }

        @Override
        public Criteria[] newArray(int size) {
            return new Criteria[size];
        }
    };

    protected Criteria(Parcel in) {
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
