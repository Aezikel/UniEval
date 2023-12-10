package com.example.unieval.data.pojo;

public class User {

    private String role, email, sessionType, firstName, lastName, title, discipline, userType, profilePhoto;
    private long phone;

    public User() {
    }

    public User(String role, String email, String sessionType) {
        this.role = role;
        this.email = email;
        this.sessionType = sessionType;
    }

    public User(String firstName, String profilePhoto) {
        this.firstName = firstName;
        this.profilePhoto = profilePhoto;
    }

    public User(String title, String firstName, String lastName, long phone, String discipline) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.discipline = discipline;
    }

    public User(String role, String email, String firstName, String lastName, String userType) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public User(String role, String email, String firstName, String lastName, String userType, String profilePhoto) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.profilePhoto = profilePhoto;
    }

    public User(String role, String email, String firstName, String lastName, String title, int phone, String discipline, String userType, String profilePhoto) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.phone = phone;
        this.discipline = discipline;
        this.userType = userType;
        this.profilePhoto = profilePhoto;
    }

    public User(String role, String email, String firstName, String lastName, String title, String discipline, String userType) {
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.discipline = discipline;
        this.userType = userType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
