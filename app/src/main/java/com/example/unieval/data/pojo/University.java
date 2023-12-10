package com.example.unieval.data.pojo;

import java.util.HashMap;

public class University {

    private String universityId;
    private Profile universityProfile;
    private CriteriaScore universityCriteriaScore;
    private HashMap<String, Course> universityCourse;
    private HashMap<String, Research> universityResearch;

    public University() {
    }

    public University(String universityId, Profile universityProfile, CriteriaScore universityCriteriaScore, HashMap<String, Course> universityCourse, HashMap<String, Research> universityResearch) {
        this.universityId = universityId;
        this.universityProfile = universityProfile;
        this.universityCriteriaScore = universityCriteriaScore;
        this.universityCourse = universityCourse;
        this.universityResearch = universityResearch;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public Profile getUniversityProfile() {
        return universityProfile;
    }

    public void setUniversityProfile(Profile universityProfile) {
        this.universityProfile = universityProfile;
    }

    public CriteriaScore getUniversityCriteriaScore() {
        return universityCriteriaScore;
    }

    public void setUniversityCriteriaScore(CriteriaScore universityCriteriaScore) {
        this.universityCriteriaScore = universityCriteriaScore;
    }

    public HashMap<String, Course> getUniversityCourse() {
        return universityCourse;
    }

    public void setUniversityCourse(HashMap<String, Course> universityCourse) {
        this.universityCourse = universityCourse;
    }

    public HashMap<String, Research> getUniversityResearch() {
        return universityResearch;
    }

    public void setUniversityResearch(HashMap<String, Research> universityResearch) {
        this.universityResearch = universityResearch;
    }
}
