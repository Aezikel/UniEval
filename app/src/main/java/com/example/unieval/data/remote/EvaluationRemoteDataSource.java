package com.example.unieval.data.remote;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unieval.data.BaseDataSource;
import com.example.unieval.data.pojo.Review;
import com.example.unieval.data.pojo.University;
import com.example.unieval.data.pojo.User;
import com.example.unieval.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EvaluationRemoteDataSource implements BaseDataSource {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    public EvaluationRemoteDataSource() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void saveUser(String userId, User user) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    databaseReference.setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public LiveData<User> getUser(String userId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId);
        final MutableLiveData<User> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                data.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<Boolean> isUserValid(String email, String userRole) {
        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.KEY_USERS);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();
        usersRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<User> user = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        User currentUser = dataSnapshot1.getValue(User.class);
                        user.add(currentUser);
                    }
                    String role = user.get(0).getRole();
                    if (role.equals(userRole)) {
                        data.setValue(true);
                    } else {
                        data.setValue(false);
                    }

                } else {
                    // user with given email not found
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return data;
    }

    @Override
    public void updateUserPhoto(String userId, Uri uri, String currentPhoto) {
        StorageReference storageReference = firebaseStorage.getReference(Constants.PATH_PROFILE_PHOTO);
        final StorageReference profilePhotoRef = storageReference.child(userId + "/" + uri.getLastPathSegment());

        profilePhotoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference photoRef = firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_PROFILE_PHOTO);
                        if (currentPhoto != null) {
                            deleteExistingPhoto(currentPhoto);
                        }
                        photoRef.setValue(uri.toString());
                    }
                });
            }
        });
    }

    public void deleteExistingPhoto(String photoUrl) {
        StorageReference ref = firebaseStorage.getReferenceFromUrl(photoUrl);
        ref.delete();
    }

    @Override
    public void updateUser(String userId, User user) {
        firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_USER_TITLE).setValue(user.getTitle());
        firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_USER_FIRSTNAME).setValue(user.getFirstName());
        firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_USER_LASTNAME).setValue(user.getLastName());
        firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_USER_PHONE).setValue(user.getPhone());
        firebaseDatabase.getReference(Constants.KEY_USERS + "/" + userId + "/" + Constants.KEY_USER_DISCIPLINE).setValue(user.getDiscipline());
    }

    @Override
    public String getUniversityId() {
        final DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        return databaseReference.push().getKey();
    }

    @Override
    public LiveData<String> saveUniversityPhoto(String universityId, Uri uri) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        StorageReference storageReference = firebaseStorage.getReference(Constants.PATH_UNIVERSITY_PHOTO);
        final StorageReference universityPhotoRef = storageReference.child(universityId + "/" + uri.getLastPathSegment());

        universityPhotoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                universityPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        data.setValue(uri.toString());
                    }
                });
            }
        });
        return data;
    }

    @Override
    public void saveUniversity(String universityId, University university) {
        firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId).setValue(university);
    }

    @Override
    public void updateUniversity(String universityId, University university) {
        firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_UNIVERSITY_PROFILE).setValue(university.getUniversityProfile());
        firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_UNIVERSITY_CRITERIA_SCORE).setValue(university.getUniversityCriteriaScore());
        firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_UNIVERSITY_COURSE).setValue(university.getUniversityCourse());
        firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_UNIVERSITY_RESEARCH).setValue(university.getUniversityResearch());
    }

    @Override
    public LiveData<List<University>> getAllUniversity() {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        final MutableLiveData<List<University>> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<University> universities = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    University university = dataSnapshot1.getValue(University.class);
                    universities.add(university);
                }
                data.setValue(universities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<University> getUniversity(String universityId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId);
        final MutableLiveData<University> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                University university = dataSnapshot.getValue(University.class);
                data.setValue(university);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public LiveData<String> saveReviewPhoto(String reviewId, Uri uri) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        StorageReference storageReference = firebaseStorage.getReference(Constants.PATH_REVIEW_PHOTO);
        final StorageReference reviewPhotoRef = storageReference.child(reviewId + "/" + uri.getLastPathSegment());

        reviewPhotoRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reviewPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        data.setValue(uri.toString());
                    }
                });
            }
        });
        return data;
    }

    @Override
    public String getReviewId() {
        final DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_REVIEW);
        return databaseReference.push().getKey();
    }

    @Override
    public void saveReview(String reviewId, Review review) {
        firebaseDatabase.getReference(Constants.KEY_REVIEW + "/" + reviewId).setValue(review);
    }

    @Override
    public LiveData<List<Review>> getAllUniversityReview(String universityId) {
        DatabaseReference reviewRef = firebaseDatabase.getReference(Constants.KEY_REVIEW);
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();
        reviewRef.orderByChild(Constants.KEY_UNIVERSITY_ID).equalTo(universityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Review review = dataSnapshot1.getValue(Review.class);
                    reviews.add(review);
                }
                data.setValue(reviews);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Review> getReview(String reviewId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_REVIEW + "/" + reviewId);
        final MutableLiveData<Review> data = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Review review = dataSnapshot.getValue(Review.class);
                data.setValue(review);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return data;
    }

    @Override
    public void addToFavourite(String universityId, String userId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_USERS);
        databaseReference.child(userId).setValue(true);
    }

    @Override
    public void removeFromFavourite(String universityId, String userId) {
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId + "/" + Constants.KEY_USERS);
        databaseReference.child(userId).setValue(null);
    }

    @Override
    public LiveData<List<University>> getFavouriteUniversity(String userId) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        final MutableLiveData<List<University>> data = new MutableLiveData<>();

        universityRef.orderByChild(Constants.KEY_USERS + "/" + userId).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<University> universities = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    University university = dataSnapshot1.getValue(University.class);
                    universities.add(university);
                }
                data.setValue(universities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<Boolean> isFavourite(String userId, String universityId) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY + "/" + universityId);
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        universityRef.orderByChild(userId).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //yes
                    data.setValue(true);
                } else {
                    data.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

    @Override
    public LiveData<List<University>> queryUniversity(String query) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        final MutableLiveData<List<University>> data = new MutableLiveData<>();

        universityRef.orderByChild(Constants.KEY_UNIVERSITY_PROFILE + "/" + Constants.KEY_UNIVERSITY_PROFILE_NAME).startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<University> universities = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            University university = dataSnapshot1.getValue(University.class);
                            universities.add(university);
                        }
                        data.setValue(universities);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return data;
    }

    @Override
    public LiveData<List<University>> getUniversityByCourseTitle(String query) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        final MutableLiveData<List<University>> data = new MutableLiveData<>();

        universityRef.orderByChild("universityCourse/" + query + "/courseTitle").equalTo(query).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<University> universities = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    University university = dataSnapshot1.getValue(University.class);
                    universities.add(university);
                }
                data.setValue(universities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return data;
    }

    @Override
    public LiveData<List<University>> getUniversityByResearchTitle(String query) {
        DatabaseReference universityRef = firebaseDatabase.getReference(Constants.KEY_UNIVERSITY);
        final MutableLiveData<List<University>> data = new MutableLiveData<>();

        universityRef.orderByChild("universityResearch/" + query + "/researchTitle").equalTo(query).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<University> universities = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    University university = dataSnapshot1.getValue(University.class);
                    universities.add(university);
                }
                data.setValue(universities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return data;
    }


}