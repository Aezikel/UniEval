package com.example.unieval.data;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.example.unieval.data.pojo.Review;
import com.example.unieval.data.pojo.University;
import com.example.unieval.data.pojo.User;

import java.util.List;

public interface BaseDataSource {
    void saveUser(String userId, User user);

    LiveData<User> getUser(String userId);

    LiveData<Boolean> isUserValid(String email, String userRole);

    void updateUserPhoto(String userId, Uri uri, String currentPhoto);

    void updateUser(String userId, User user);

    String getUniversityId();

    LiveData<String> saveUniversityPhoto(String universityId, Uri uri);

    void saveUniversity(String universityId, University university);

    void updateUniversity(String universityId, University university);

    LiveData<List<University>> getAllUniversity();

    LiveData<University> getUniversity(String universityId);

    LiveData<String> saveReviewPhoto(String reviewId, Uri uri);

    String getReviewId();

    void saveReview(String reviewId, Review review);

    LiveData<List<Review>> getAllUniversityReview(String universityId);

    LiveData<Review> getReview(String reviewId);

    void addToFavourite(String universityId, String userId);

    void removeFromFavourite(String universityId, String userId);

    LiveData<List<University>> getFavouriteUniversity(String userId);

    LiveData<Boolean> isFavourite(String userId, String universityId);

    LiveData<List<University>> queryUniversity(String query);

    LiveData<List<University>> getUniversityByCourseTitle(String query);

    LiveData<List<University>> getUniversityByResearchTitle(String query);

}
