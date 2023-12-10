package com.example.unieval.data.pojo;

public class Review {

    private String reviewId, universityId, reviewerPhoto, reviewerName, reviewerClass, reviewMessage;
    private int reviewerRating;

    public Review() {
    }

    public Review(String reviewId, String universityId, String reviewerPhoto, String reviewerName, String reviewerClass, String reviewMessage, int reviewerRating) {
        this.reviewId = reviewId;
        this.universityId = universityId;
        this.reviewerPhoto = reviewerPhoto;
        this.reviewerName = reviewerName;
        this.reviewerClass = reviewerClass;
        this.reviewMessage = reviewMessage;
        this.reviewerRating = reviewerRating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getReviewerPhoto() {
        return reviewerPhoto;
    }

    public void setReviewerPhoto(String reviewerPhoto) {
        this.reviewerPhoto = reviewerPhoto;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerClass() {
        return reviewerClass;
    }

    public void setReviewerClass(String reviewerClass) {
        this.reviewerClass = reviewerClass;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public int getReviewerRating() {
        return reviewerRating;
    }

    public void setReviewerRating(int reviewerRating) {
        this.reviewerRating = reviewerRating;
    }
}
