package com.example.unieval.ui.user.universitydetails.research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unieval.R;
import com.example.unieval.data.pojo.Research;
import com.example.unieval.util.Constants;

import java.util.HashMap;
import java.util.List;

public class ResearchExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Research>> expandableListDetail;

    public ResearchExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<Research>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_group_discipline, null);
        }
        ImageView disciplineImageView = convertView.findViewById(R.id.discipline_imageView);
        TextView disciplineResearchAmount = convertView.findViewById(R.id.discipline_courseSizeTextView);
        TextView disciplineTitle = convertView.findViewById(R.id.discipline_textView);

        if (listTitle.equals(Constants.DISCIPLINE_ENGINEERING)) {
            disciplineImageView.setImageResource(R.drawable.baseline_build_24);
        } else if (listTitle.equals(Constants.DISCIPLINE_ART_AND_HUMANITY)) {
            disciplineImageView.setImageResource(R.drawable.baseline_format_paint_24);
        } else if (listTitle.equals(Constants.DISCIPLINE_BUSINESS)) {
            disciplineImageView.setImageResource(R.drawable.baseline_monetization_on_24);
        } else {
            disciplineImageView.setImageResource(R.drawable.baseline_science_24);
        }

        int size = expandableListDetail.get(listTitle).size();
        String amount = size + " Research Topic";
        disciplineResearchAmount.setText(amount);

        disciplineTitle.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Research expandedListResearch = (Research) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_child_discipline, null);
        }
        TextView researchTitleTextView = convertView.findViewById(R.id.course_titleTextView);
        TextView researchPriceTextView = convertView.findViewById(R.id.course_priceTextView);

        researchTitleTextView.setText(expandedListResearch.getResearchTitle());
        researchPriceTextView.setText(expandedListResearch.getResearchSalary());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
