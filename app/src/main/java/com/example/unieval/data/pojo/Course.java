package com.example.unieval.data.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String courseTitle, coursePrice, courseDiscipline;

    public Course() {
    }

    public Course(String courseTitle, String coursePrice) {
        this.courseTitle = courseTitle;
        this.coursePrice = coursePrice;
    }

    public Course(String courseTitle, String coursePrice, String courseDiscipline) {
        this.courseTitle = courseTitle;
        this.coursePrice = coursePrice;
        this.courseDiscipline = courseDiscipline;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    protected Course(Parcel in) {
        courseTitle = in.readString();
        coursePrice = in.readString();
        courseDiscipline = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseTitle);
        dest.writeString(coursePrice);
        dest.writeString(courseDiscipline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCourseDiscipline() {
        return courseDiscipline;
    }

    public void setCourseDiscipline(String courseDiscipline) {
        this.courseDiscipline = courseDiscipline;
    }
}
