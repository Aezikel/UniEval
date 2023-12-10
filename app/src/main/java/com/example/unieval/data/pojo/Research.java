package com.example.unieval.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Research implements Parcelable {

    private String researchTitle, researchSalary, researchDiscipline;

    public Research() {
    }

    public Research(String researchTitle, String researchSalary) {
        this.researchTitle = researchTitle;
        this.researchSalary = researchSalary;
    }

    public Research(String researchTitle, String researchSalary, String researchDiscipline) {
        this.researchTitle = researchTitle;
        this.researchSalary = researchSalary;
        this.researchDiscipline = researchDiscipline;
    }

    public static final Creator<Research> CREATOR = new Creator<Research>() {
        @Override
        public Research createFromParcel(Parcel in) {
            return new Research(in);
        }

        @Override
        public Research[] newArray(int size) {
            return new Research[size];
        }
    };

    protected Research(Parcel in) {
        researchTitle = in.readString();
        researchSalary = in.readString();
        researchDiscipline = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(researchTitle);
        dest.writeString(researchSalary);
        dest.writeString(researchDiscipline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getResearchTitle() {
        return researchTitle;
    }

    public void setResearchTitle(String researchTitle) {
        this.researchTitle = researchTitle;
    }

    public String getResearchSalary() {
        return researchSalary;
    }

    public void setResearchSalary(String researchSalary) {
        this.researchSalary = researchSalary;
    }

    public String getResearchDiscipline() {
        return researchDiscipline;
    }

    public void setResearchDiscipline(String researchDiscipline) {
        this.researchDiscipline = researchDiscipline;
    }
}
