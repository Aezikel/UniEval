package com.example.unieval.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.pojo.Criteria;

import java.util.List;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.CriteriaViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<Criteria> criteriaList;

    public CriteriaAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public CriteriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_university, parent, false);
        return new CriteriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaViewHolder holder, int position) {
        if (criteriaList != null) {
            Criteria currentCriteria = criteriaList.get(position);
            Glide.with(holder.uniImageView.getContext()).load(currentCriteria.getUniversity().getUniversityProfile().getPhoto()).into(holder.uniImageView);
            holder.uniName.setText(currentCriteria.getUniversity().getUniversityProfile().getName());
            holder.uniLocation.setText(currentCriteria.getScore() + " criteria passed");
            holder.uniRank.setText(String.valueOf(criteriaList.indexOf(currentCriteria) + 1));
        }
    }

    @Override
    public int getItemCount() {
        if (criteriaList != null) {
            return criteriaList.size();
        } else {
            return 0;
        }
    }

    public void setList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(Criteria criteria);

    }

    class CriteriaViewHolder extends RecyclerView.ViewHolder {

        ImageView uniImageView;
        TextView uniName, uniLocation, uniRank;

        public CriteriaViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImageView = itemView.findViewById(R.id.uni_listItem_university_imageView);
            uniName = itemView.findViewById(R.id.uni_listItem_university_textview);
            uniLocation = itemView.findViewById(R.id.uni_listItem_university_textview2);
            uniRank = itemView.findViewById(R.id.uni_listItem_university_textview3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnclickListener.onListItemClick(criteriaList.get(clickedPosition));
                }
            });
        }
    }

}
