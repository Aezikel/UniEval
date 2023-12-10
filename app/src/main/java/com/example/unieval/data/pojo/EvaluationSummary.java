package com.example.unieval.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class EvaluationSummary implements Parcelable {

    public static final Creator<EvaluationSummary> CREATOR = new Creator<EvaluationSummary>() {
        @Override
        public EvaluationSummary createFromParcel(Parcel in) {
            return new EvaluationSummary(in);
        }

        @Override
        public EvaluationSummary[] newArray(int size) {
            return new EvaluationSummary[size];
        }
    };
    private String criteria, userValue, dbValue;
    private boolean criteriaMet;

    public EvaluationSummary(String criteria, String userValue, String dbValue, boolean criteriaMet) {
        this.criteria = criteria;
        this.userValue = userValue;
        this.dbValue = dbValue;
        this.criteriaMet = criteriaMet;
    }

    protected EvaluationSummary(Parcel in) {
        criteria = in.readString();
        userValue = in.readString();
        dbValue = in.readString();
        criteriaMet = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(criteria);
        dest.writeString(userValue);
        dest.writeString(dbValue);
        dest.writeByte((byte) (criteriaMet ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getUserValue() {
        return userValue;
    }

    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public void setDbValue(String dbValue) {
        this.dbValue = dbValue;
    }

    public boolean isCriteriaMet() {
        return criteriaMet;
    }

    public void setCriteriaMet(boolean criteriaMet) {
        this.criteriaMet = criteriaMet;
    }
}
