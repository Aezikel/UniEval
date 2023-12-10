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
import com.example.unieval.data.pojo.University;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<University> universities;

    public UniversityAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public UniversityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_university, parent, false);
        return new UniversityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityViewHolder holder, int position) {
        if (universities != null) {
            University currentUniversity = universities.get(position);
            Glide.with(holder.uniImageView.getContext()).load(currentUniversity.getUniversityProfile().getPhoto()).into(holder.uniImageView);
            holder.uniName.setText(currentUniversity.getUniversityProfile().getName());
            holder.uniLocation.setText(currentUniversity.getUniversityProfile().getLocation());
            holder.uniRank.setText(String.valueOf(currentUniversity.getUniversityProfile().getRank()));
        }
    }

    @Override
    public int getItemCount() {
        if (universities != null) {
            return universities.size();
        } else {
            return 0;
        }
    }

    public void setList(List<University> universities) {
        this.universities = universities;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(String universityId);

    }

    class UniversityViewHolder extends RecyclerView.ViewHolder {

        ImageView uniImageView;
        TextView uniName, uniLocation, uniRank;

        public UniversityViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImageView = itemView.findViewById(R.id.uni_listItem_university_imageView);
            uniName = itemView.findViewById(R.id.uni_listItem_university_textview);
            uniLocation = itemView.findViewById(R.id.uni_listItem_university_textview2);
            uniRank = itemView.findViewById(R.id.uni_listItem_university_textview3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnclickListener.onListItemClick(universities.get(clickedPosition).getUniversityId());
                }
            });
        }
    }

}
