package com.example.unieval.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class CriteriaScore implements Parcelable {

    public static final Creator<CriteriaScore> CREATOR = new Creator<CriteriaScore>() {
        @Override
        public CriteriaScore createFromParcel(Parcel in) {
            return new CriteriaScore(in);
        }

        @Override
        public CriteriaScore[] newArray(int size) {
            return new CriteriaScore[size];
        }
    };
    private boolean internship;
    private int studentTeacherRatio, graduateEmploymentRatio, workLifeBalance;
    private boolean professionalDevelopment;
    private int facilityScore;

    public CriteriaScore() {
    }

    public CriteriaScore(boolean internship, int studentTeacherRatio, int graduateEmploymentRatio, int workLifeBalance, boolean professionalDevelopment, int facilityScore) {
        this.internship = internship;
        this.studentTeacherRatio = studentTeacherRatio;
        this.graduateEmploymentRatio = graduateEmploymentRatio;
        this.workLifeBalance = workLifeBalance;
        this.professionalDevelopment = professionalDevelopment;
        this.facilityScore = facilityScore;
    }

    protected CriteriaScore(Parcel in) {
        internship = in.readByte() != 0;
        studentTeacherRatio = in.readInt();
        graduateEmploymentRatio = in.readInt();
        workLifeBalance = in.readInt();
        professionalDevelopment = in.readByte() != 0;
        facilityScore = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (internship ? 1 : 0));
        dest.writeInt(studentTeacherRatio);
        dest.writeInt(graduateEmploymentRatio);
        dest.writeInt(workLifeBalance);
        dest.writeByte((byte) (professionalDevelopment ? 1 : 0));
        dest.writeInt(facilityScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isInternship() {
        return internship;
    }

    public void setInternship(boolean internship) {
        this.internship = internship;
    }

    public int getStudentTeacherRatio() {
        return studentTeacherRatio;
    }

    public void setStudentTeacherRatio(int studentTeacherRatio) {
        this.studentTeacherRatio = studentTeacherRatio;
    }

    public int getGraduateEmploymentRatio() {
        return graduateEmploymentRatio;
    }

    public void setGraduateEmploymentRatio(int graduateEmploymentRatio) {
        this.graduateEmploymentRatio = graduateEmploymentRatio;
    }

    public int getWorkLifeBalance() {
        return workLifeBalance;
    }

    public void setWorkLifeBalance(int workLifeBalance) {
        this.workLifeBalance = workLifeBalance;
    }

    public boolean isProfessionalDevelopment() {
        return professionalDevelopment;
    }

    public void setProfessionalDevelopment(boolean professionalDevelopment) {
        this.professionalDevelopment = professionalDevelopment;
    }

    public int getFacilityScore() {
        return facilityScore;
    }

    public void setFacilityScore(int facilityScore) {
        this.facilityScore = facilityScore;
    }
}
