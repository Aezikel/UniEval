package com.example.unieval.ui.user.criteriasearch;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unieval.R;
import com.example.unieval.databinding.FragmentCriteriaSearchBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CriteriaSearchFragment extends Fragment {

    FragmentCriteriaSearchBinding binding;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_criteria_search, container, false);


        return binding.getRoot();
    }
}