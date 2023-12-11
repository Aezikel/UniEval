package com.example.unieval.ui.admin.manageuniversity.course;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.unieval.R;
import com.example.unieval.data.pojo.Course;
import com.example.unieval.databinding.ActivityUniversityCourseBinding;
import com.example.unieval.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UniversityCourseActivity extends AppCompatActivity {

    ActivityUniversityCourseBinding binding;
    UniversityCourseAdapter engineeringCourseAdapter;
    UniversityCourseAdapter artCourseAdapter;
    UniversityCourseAdapter scienceCourseAdapter;
    UniversityCourseAdapter businessCourseAdapter;
    List<Course> engineeringList;
    List<String> engineeringcourseTitleList;
    List<Course> artList;
    List<String> artcourseTitleList;
    List<Course> scienceList;
    List<String> sciencecourseTitleList;
    List<Course> businessList;
    List<String> businesscourseTitleList;
    List<Course> allCourse;
    List<Course> courseResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_university_course);

        initAdapter();

        courseResult = getIntent().getParcelableArrayListExtra(Constants.KEY_INTENT_COURSE);

        engineeringcourseTitleList = arrayToList(getResources().getStringArray(R.array.Engineering_Courses));
        artcourseTitleList = arrayToList(getResources().getStringArray(R.array.Art_and_Humanities_Courses));
        sciencecourseTitleList = arrayToList(getResources().getStringArray(R.array.Science_Courses));
        businesscourseTitleList = arrayToList(getResources().getStringArray(R.array.Business_Courses));

        if (courseResult != null) {
            loadEntries(courseResult);
        }
        else {
            engineeringList = new ArrayList<>();
            artList = new ArrayList<>();
            scienceList = new ArrayList<>();
            businessList = new ArrayList<>();
        }

        allCourse = new ArrayList<>();

        engineeringCourseAdapter.setList(engineeringList);
        artCourseAdapter.setList(artList);
        scienceCourseAdapter.setList(scienceList);
        businessCourseAdapter.setList(businessList);

        binding.uniCourseToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.uniCourseToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        builder.setTitle("Add a Course");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_course, null);

        final AutoCompleteTextView courseTitle = dialogView.findViewById(R.id.add_course_title);
        final TextInputEditText coursePrice = dialogView.findViewById(R.id.add_course_price);

        builder.setView(dialogView);

        String[] courses;
        if (discipline.equals(Constants.DISCIPLINE_ENGINEERING)) {
            courses = listToArray(engineeringcourseTitleList);
        } else if (discipline.equals(Constants.DISCIPLINE_ART)) {
            courses = listToArray(artcourseTitleList);
        } else if (discipline.equals(Constants.DISCIPLINE_SCIENCE)) {
            courses = listToArray(sciencecourseTitleList);
        } else {
            courses = listToArray(businesscourseTitleList);
        }

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(UniversityCourseActivity.this, R.layout.list_item, courses);
        courseTitle.setAdapter(courseAdapter);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!TextUtils.isEmpty(courseTitle.getText().toString()) && !TextUtils.isEmpty(coursePrice.getText().toString())) {
                    Course course = new Course(courseTitle.getText().toString(), coursePrice.getText().toString(), discipline);
                    if (discipline.equals(Constants.DISCIPLINE_ENGINEERING)) {
                        engineeringList.add(course);
                        engineeringcourseTitleList.remove(courseTitle.getText().toString());
                        engineeringCourseAdapter.notifyDataSetChanged();
                    } else if (discipline.equals(Constants.DISCIPLINE_ART)) {
                        artList.add(course);
                        artcourseTitleList.remove(courseTitle.getText().toString());
                        artCourseAdapter.notifyDataSetChanged();
                    } else if (discipline.equals(Constants.DISCIPLINE_SCIENCE)) {
                        scienceList.add(course);
                        sciencecourseTitleList.remove(courseTitle.getText().toString());
                        scienceCourseAdapter.notifyDataSetChanged();
                    } else {
                        businessList.add(course);
                        businesscourseTitleList.remove(courseTitle.getText().toString());
                        businessCourseAdapter.notifyDataSetChanged();
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

        engineeringCourseAdapter = new UniversityCourseAdapter(new UniversityCourseAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                engineeringCourseAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            engineeringcourseTitleList.add(engineeringCourseAdapter.getCourse(clickedPosition).getCourseTitle());
                            engineeringCourseAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        artCourseAdapter = new UniversityCourseAdapter(new UniversityCourseAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                artCourseAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            artcourseTitleList.add(artCourseAdapter.getCourse(clickedPosition).getCourseTitle());
                            artCourseAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        scienceCourseAdapter = new UniversityCourseAdapter(new UniversityCourseAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                scienceCourseAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            sciencecourseTitleList.add(scienceCourseAdapter.getCourse(clickedPosition).getCourseTitle());
                            scienceCourseAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        businessCourseAdapter = new UniversityCourseAdapter(new UniversityCourseAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedPosition, ImageView menu) {
                businessCourseAdapter.createPopupMenu(menu).setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.course_delete) {
                            businesscourseTitleList.add(businessCourseAdapter.getCourse(clickedPosition).getCourseTitle());
                            businessCourseAdapter.remove(clickedPosition);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        binding.engineeringRecyclerView.setAdapter(engineeringCourseAdapter);
        binding.artRecyclerView.setAdapter(artCourseAdapter);
        binding.scienceRecyclerView.setAdapter(scienceCourseAdapter);
        binding.businessRecyclerView.setAdapter(businessCourseAdapter);
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

    public void loadEntries(List<Course> courseResult) {
        binding.uniCourseToolbar.setTitle("Edit Course");
        engineeringList = segmentList(courseResult, Constants.DISCIPLINE_ENGINEERING);
        artList = segmentList(courseResult, Constants.DISCIPLINE_ART);
        scienceList = segmentList(courseResult, Constants.DISCIPLINE_SCIENCE);
        businessList = segmentList(courseResult, Constants.DISCIPLINE_BUSINESS);

        updateTitleList(courseResult, engineeringcourseTitleList, Constants.DISCIPLINE_ENGINEERING);
        updateTitleList(courseResult, artcourseTitleList, Constants.DISCIPLINE_ART);
        updateTitleList(courseResult, sciencecourseTitleList, Constants.DISCIPLINE_SCIENCE);
        updateTitleList(courseResult, businesscourseTitleList, Constants.DISCIPLINE_BUSINESS);
    }

    public List<Course> segmentList(List<Course> masterList, String discipline) {
        List<Course> disciplineList = new ArrayList<>();
        for (Course c : masterList) {
            if (c.getCourseDiscipline().equals(discipline)) {
                // create discipline list
                disciplineList.add(c);
            }
        }
        return disciplineList;
    }

    public void updateTitleList(List<Course> masterList, List<String> masterTitleList, String discipline) {
        List<String> disciplineTitleList = new ArrayList<>();
        for (Course c : masterList) {
            if (c.getCourseDiscipline().equals(discipline)) {
                disciplineTitleList.add(c.getCourseTitle());
            }
        }
        for (String title : disciplineTitleList) {
            if (masterTitleList.contains(title)) {
                masterTitleList.remove(title);
            }
        }
    }

    public void validateCredentials() {
        if (!engineeringList.isEmpty() && !artList.isEmpty() && !scienceList.isEmpty() && !businessList.isEmpty()) {
            allCourse.addAll(engineeringList);
            allCourse.addAll(artList);
            allCourse.addAll(scienceList);
            allCourse.addAll(businessList);
            Intent returnIntent = new Intent();
            returnIntent.putParcelableArrayListExtra(Constants.KEY_INTENT_COURSE, (ArrayList<? extends Parcelable>) allCourse);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(UniversityCourseActivity.this, "Please add a course for each discipline", Toast.LENGTH_SHORT).show();
        }
    }

}