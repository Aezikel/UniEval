package com.example.unieval.ui.admin.manageuniversity.research;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.unieval.R;
import com.example.unieval.data.pojo.Research;
import com.example.unieval.databinding.ActivityUniversityResearchBinding;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Arrays;
import java.util.List;

public class UniversityResearchActivity extends AppCompatActivity {

    ActivityUniversityResearchBinding binding;
    UniversityResearchAdapter engineeringResearchAdapter;
    UniversityResearchAdapter artResearchAdapter;
    UniversityResearchAdapter scienceResearchAdapter;
    UniversityResearchAdapter businessResearchAdapter;
    List<Research> engineeringList;
    List<String> engineeringResearchTitleList;
    List<Research> artList;
    List<String> artResearchTitleList;
    List<Research> scienceList;
    List<String> scienceResearchTitleList;
    List<Research> businessList;
    List<String> businessResearchTitleList;
    List<Research> allResearch;
    List<Research> researchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_research);

        initAdapter();

        researchResult = getIntent().getParcelableArrayListExtra(Constants.KEY_INTENT_RESEARCH);

        // Use courses list for now until research list is added
        engineeringResearchTitleList = arrayToList(getResources().getStringArray(R.array.Engineering_Research));
        artResearchTitleList = arrayToList(getResources().getStringArray(R.array.Art_and_Humanities_Research));
        scienceResearchTitleList = arrayToList(getResources().getStringArray(R.array.Science_Research));
        businessResearchTitleList = arrayToList(getResources().getStringArray(R.array.Business_Research));

        if (researchResult != null) {
            loadEntries(researchResult);
        } else {
            engineeringList = new ArrayList<>();
            artList = new ArrayList<>();
            scienceList = new ArrayList<>();
            businessList = new ArrayList<>();
        }

        allResearch = new ArrayList<>();

        engineeringResearchAdapter.setList(engineeringList);
        artResearchAdapter.setList(artList);
        scienceResearchAdapter.setList(scienceList);
        businessResearchAdapter.setList(businessList);

        binding.uniResearchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniResearchToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.done) {
                    validateCredentials();
                }
                return false;
            }
        });

        binding.engineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog(Constants.DISCIPLINE_ENGINEERING);
            }
        });

        binding.artButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog(Constants.DISCIPLINE_ART);
            }
        });

        binding.scienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog(Constants.DISCIPLINE_SCIENCE);
            }
        });

        binding.buisnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog(Constants.DISCIPLINE_BUSINESS);
            }
        });

    }

    public void createInputDialog(String discipline) {
        // Create a MaterialAlertDialog and set the title and custom view for collecting input and also click listeners
        // for the positive and negative buttons on the dialog.
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add Research");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_course, null);

        final AutoCompleteTextView courseTitle = dialogView.findViewById(R.id.add_course_title);
        final TextInputEditText coursePrice = dialogView.findViewById(R.id.add_course_price);

        builder.setView(dialogView);

        String[] research;
        if (discipline.equals(Constants.DISCIPLINE_ENGINEERING)) {
            research = listToArray(engineeringResearchTitleList);
        } else if (discipline.equals(Constants.DISCIPLINE_ART)) {
            research = listToArray(artResearchTitleList);
        } else if (discipline.equals(Constants.DISCIPLINE_SCIENCE)) {
            research = listToArray(scienceResearchTitleList);
        } else {
            research = listToArray(businessResearchTitleList);
        }

        ArrayAdapter<String> researchAdapter = new ArrayAdapter<>(UniversityResearchActivity.this, R.layout.list_item, research);
        courseTitle.setAdapter(researchAdapter);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!TextUtils.isEmpty(courseTitle.getText().toString()) && !TextUtils.isEmpty(coursePrice.getText().toString())) {
                    Research research = new Research(courseTitle.getText().toString(), coursePrice.getText().toString(), discipline);
                    if (discipline.equals(Constants.DISCIPLINE_ENGINEERING)) {
                        engineeringList.add(research);
                        engineeringResearchTitleList.remove(courseTitle.getText().toString());
                        engineeringResearchAdapter.notifyDataSetChanged();
                    } else if (discipline.equals(Constants.DISCIPLINE_ART)) {
                        artList.add(research);
                        artResearchTitleList.remove(courseTitle.getText().toString());
                        artResearchAdapter.notifyDataSetChanged();
                    } else if (discipline.equals(Constants.DISCIPLINE_SCIENCE)) {
                        scienceList.add(research);
                        scienceResearchTitleList.remove(courseTitle.getText().toString());
                        scienceResearchAdapter.notifyDataSetChanged();
                    } else {
                        businessList.add(research);
                        businessResearchTitleList.remove(courseTitle.getText().toString());
                        businessResearchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void initAdapter() {
        LinearLayoutManager ellm = new LinearLayoutManager(this);
        LinearLayoutManager allm = new LinearLayoutManager(this);
        LinearLayoutManager sllm = new LinearLayoutManager(this);
        LinearLayoutManager bllm = new LinearLayoutManager(this);

        binding.engineeringRecyclerView.setLayoutManager(ellm);
        binding.artRecyclerView.setLayoutManager(allm);
        binding.scienceRecyclerView.setLayoutManager(sllm);
        binding.businessRecyclerView.setLayoutManager(bllm);

        binding.engineeringRecyclerView.setHasFixedSize(true);
        binding.artRecyclerView.setHasFixedSize(true);
        binding.scienceRecyclerView.setHasFixedSize(true);
        binding.businessRecyclerView.setHasFixedSize(true);

        engineeringResearchAdapter = new UniversityResearchAdapter(new UniversityResearchAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                engineeringResearchAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            engineeringResearchTitleList.add(engineeringResearchAdapter.getResearch(clickedPosition).getResearchTitle());
                            engineeringResearchAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        artResearchAdapter = new UniversityResearchAdapter(new UniversityResearchAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                artResearchAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            artResearchTitleList.add(artResearchAdapter.getResearch(clickedPosition).getResearchTitle());
                            artResearchAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        scienceResearchAdapter = new UniversityResearchAdapter(new UniversityResearchAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                scienceResearchAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            scienceResearchTitleList.add(scienceResearchAdapter.getResearch(clickedPosition).getResearchTitle());
                            scienceResearchAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        businessResearchAdapter = new UniversityResearchAdapter(new UniversityResearchAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                businessResearchAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            businessResearchTitleList.add(businessResearchAdapter.getResearch(clickedPosition).getResearchTitle());
                            businessResearchAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        binding.engineeringRecyclerView.setAdapter(engineeringResearchAdapter);
        binding.artRecyclerView.setAdapter(artResearchAdapter);
        binding.scienceRecyclerView.setAdapter(scienceResearchAdapter);
        binding.businessRecyclerView.setAdapter(businessResearchAdapter);
    }

    public String[] listToArray(List<String> list) {
        String[] entries = new String[list.size()];
        for (String s : list) {
            entries[list.indexOf(s)] = s;
        }
        return entries;
    }


    public List<String> arrayToList(String[] array) {
        List<String> entries = new ArrayList<>();
        entries.addAll(Arrays.asList(array));
        return entries;
    }

    public void loadEntries(List<Research> researchResult) {
        binding.uniResearchToolbar.setTitle("Edit Research");
        engineeringList = segmentList(researchResult, Constants.DISCIPLINE_ENGINEERING);
        artList = segmentList(researchResult, Constants.DISCIPLINE_ART);
        scienceList = segmentList(researchResult, Constants.DISCIPLINE_SCIENCE);
        businessList = segmentList(researchResult, Constants.DISCIPLINE_BUSINESS);

        updateTitleList(researchResult, engineeringResearchTitleList, Constants.DISCIPLINE_ENGINEERING);
        updateTitleList(researchResult, artResearchTitleList, Constants.DISCIPLINE_ART);
        updateTitleList(researchResult, scienceResearchTitleList, Constants.DISCIPLINE_SCIENCE);
        updateTitleList(researchResult, businessResearchTitleList, Constants.DISCIPLINE_BUSINESS);
    }


    public void updateTitleList(List<Research> masterList, List<String> masterTitleList, String discipline) {
        List<String> disciplineTitleList = new ArrayList<>();
        for (Research r : masterList) {
            if (r.getResearchDiscipline().equals(discipline)) {
                disciplineTitleList.add(r.getResearchTitle());
            }
        }
        for (String title : disciplineTitleList) {
            if (masterTitleList.contains(title)) {
                masterTitleList.remove(title);
            }
        }
    }


    public List<Research> segmentList(List<Research> masterList, String discipline) {
        List<Research> disciplineList = new ArrayList<>();
        for (Research r : masterList) {
            if (r.getResearchDiscipline().equals(discipline)) {
                // create discipline list
                disciplineList.add(r);
            }
        }
        return disciplineList;
    }

    public void validateCredentials() {
        if (!engineeringList.isEmpty() && !artList.isEmpty() && !scienceList.isEmpty() && !businessList.isEmpty()) {
            allResearch.addAll(engineeringList);
            allResearch.addAll(artList);
            allResearch.addAll(scienceList);
            allResearch.addAll(businessList);
            Intent returnIntent = new Intent();
            returnIntent.putParcelableArrayListExtra(Constants.KEY_INTENT_RESEARCH, (ArrayList<? extends Parcelable>) allResearch);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(UniversityResearchActivity.this, "Please add a research topic for each discipline", Toast.LENGTH_SHORT).show();
        }
    }

}