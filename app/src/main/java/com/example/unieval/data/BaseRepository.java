package com.example.unieval.data;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.example.unieval.data.pojo.Review;
import com.example.unieval.data.pojo.University;
import com.example.unieval.data.pojo.User;
import com.example.unieval.data.remote.EvaluationRemoteDataSource;

import java.util.List;

public class BaseRepository {

    private final EvaluationRemoteDataSource remoteDataSource;

    public BaseRepository() {
        remoteDataSource = new EvaluationRemoteDataSource();
    }

    public void saveUser(String userId, User user) {
        remoteDataSource.saveUser(userId, user);
    }

    public LiveData<User> getUser(String userId) {
        return remoteDataSource.getUser(userId);
    }

    public LiveData<Boolean> isUserValid(String email, String userRole) {
        return remoteDataSource.isUserValid(email, userRole);
    }

    public void updateProfilePhoto(String userId, Uri uri, String currentPhoto) {
        remoteDataSource.updateUserPhoto(userId, uri, currentPhoto);
    }

    public void deleteExistingPhoto(String photoUrl) {
        remoteDataSource.deleteExistingPhoto(photoUrl);
    }

    public void updateUser(String userId, User user) {
        remoteDataSource.updateUser(userId, user);
    }

    public String getUniversityId() {
        return remoteDataSource.getUniversityId();
    }

    public LiveData<String> saveUniversityPhoto(String universityId, Uri uri) {
        return remoteDataSource.saveUniversityPhoto(universityId, uri);
    }

    public void saveUniversity(String universityId, University university) {
        remoteDataSource.saveUniversity(universityId, university);
    }

    public void updateUniversity(String universityId, University university) {
        remoteDataSource.updateUniversity(universityId, university);
    }

    public LiveData<List<University>> getAllUniversity() {
        return remoteDataSource.getAllUniversity();
    }

    public LiveData<University> getUniversity(String universityId) {
        return remoteDataSource.getUniversity(universityId);
    }

    public LiveData<String> saveReviewPhoto(String reviewId, Uri uri) {
        return remoteDataSource.saveReviewPhoto(reviewId, uri);
    }

    public String getReviewId() {
        return remoteDataSource.getReviewId();
    }

    public void saveReview(String reviewId, Review review) {
        remoteDataSource.saveReview(reviewId, review);
    }

    public LiveData<List<Review>> getAllUniversityReview(String universityId) {
        return remoteDataSource.getAllUniversityReview(universityId);
    }

    public LiveData<Review> getReview(String reviewId) {
        return remoteDataSource.getReview(reviewId);
    }

    public void addToFavourite(String universityId, String userId) {
        remoteDataSource.addToFavourite(universityId, userId);
    }

    public void removeFromFavourite(String universityId, String userId) {
        remoteDataSource.removeFromFavourite(universityId, userId);
    }

    public LiveData<List<University>> getFavouriteUniversity(String userId) {
        return remoteDataSource.getFavouriteUniversity(userId);
    }

    public LiveData<Boolean> isFavourite(String userId, String universityId) {
        return remoteDataSource.isFavourite(userId, universityId);
    }

    public LiveData<List<University>> queryUniversity(String query) {
        return remoteDataSource.queryUniversity(query);
    }

    public LiveData<List<University>> getUniversityByCourseTitle(String query) {
        return remoteDataSource.getUniversityByCourseTitle(query);
    }

    public LiveData<List<University>> getUniversityByResearchTitle(String query) {
        return remoteDataSource.getUniversityByResearchTitle(query);
    }


}
