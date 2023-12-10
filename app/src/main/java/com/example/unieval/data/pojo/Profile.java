package com.example.unieval.data.pojo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    private Uri photoUri;
    private String photo;
    private String name;
    private String overview;
    private String mission;
    private int rank;
    private float rating;
    private String location, email;
    private long phone;

    public Profile() {
    }

    public Profile(Uri photoUri, String name, String overview, String mission, int rank, float rating, String location, String email, long phone) {
        this.photoUri = photoUri;
        this.name = name;
        this.overview = overview;
        this.mission = mission;
        this.rank = rank;
        this.rating = rating;
        this.location = location;
        this.email = email;
        this.phone = phone;
    }

    public Profile(String photo, String name, String overview, String mission, int rank, float rating, String location, String email, long phone) {
        this.photo = photo;
        this.name = name;
        this.overview = overview;
        this.mission = mission;
        this.rank = rank;
        this.rating = rating;
        this.location = location;
        this.email = email;
        this.phone = phone;
    }

    protected Profile(Parcel in) {
        photoUri = in.readParcelable(Uri.class.getClassLoader());
        photo = in.readString();
        name = in.readString();
        overview = in.readString();
        mission = in.readString();
        rank = in.readInt();
        rating = in.readFloat();
        location = in.readString();
        email = in.readString();
        phone = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photoUri, flags);
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(mission);
        dest.writeInt(rank);
        dest.writeFloat(rating);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeLong(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
