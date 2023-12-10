package com.example.unieval.ui.user.universitydetails;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.unieval.ui.user.universitydetails.about.TabAboutFragment;
import com.example.unieval.ui.user.universitydetails.courses.TabCourseFragment;
import com.example.unieval.ui.user.universitydetails.research.TabResearchFragment;
import com.example.unieval.ui.user.universitydetails.reviews.TabReviewFragment;
import com.example.unieval.util.Constants;

public class UniversityDetailAdapter extends FragmentStateAdapter {

    String userType;

    public UniversityDetailAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (userType != null) {
            if (userType.equals(Constants.USER_TYPE_STUDENT)) {
                // Return a NEW fragment instance in createFragment(int).
                if (position == 0) {
                    return new TabAboutFragment();
                } else if (position == 1) {
                    return new TabCourseFragment();
                } else {
                    return new TabReviewFragment();
                }
            } else {
                if (position == 0) {
                    return new TabAboutFragment();
                } else if (position == 1) {
                    return new TabResearchFragment();
                } else {
                    return new TabReviewFragment();
                }
            }
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
