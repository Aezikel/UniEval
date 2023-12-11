package com.example.unieval.ui.user.universitydetails.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.unieval.R;
import com.example.unieval.data.BaseRepository;
import com.example.unieval.data.pojo.University;
import com.example.unieval.databinding.FragmentTabAboutBinding;
import com.example.unieval.ui.user.universitydetails.UniversityDetailActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TabAboutFragment extends Fragment {

    FragmentTabAboutBinding binding;
    String universityId;
    BaseRepository baseRepository;

    public TabAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_about, container, false);

        baseRepository = new BaseRepository();

        UniversityDetailActivity activity = (UniversityDetailActivity) getActivity();
        universityId = activity.universityId;

        if (universityId != null) {

            baseRepository.getUniversity(universityId).observe(getViewLifecycleOwner(), new Observer<University>() {
                @Override
                public void onChanged(University university) {
                    if (university != null) {
                        binding.textView6.setText(university.getUniversityProfile().getOverview());
                        binding.textView14.setText(university.getUniversityProfile().getMission());
                        binding.textView15.setText(university.getUniversityProfile().getLocation());
                        binding.textView17.setText(String.valueOf(university.getUniversityProfile().getPhone()));
                        binding.textView18.setText(university.getUniversityProfile().getEmail());
                        binding.textView7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showMoreInfoDialog(university.getUniversityProfile().getOverview());
                            }
                        });

                        binding.textView17.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialPhoneNumber(String.valueOf(university.getUniversityProfile().getPhone()));
                            }
                        });

                        binding.textView18.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                composeEmail(university.getUniversityProfile().getEmail());
                            }
                        });


                    }
                }
            });
        }


        return binding.getRoot();
    }

    public void showMoreInfoDialog(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Overview");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void dialPhoneNumber(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String address) {
        String[] addresses = {address};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}