package com.example.unieval.ui.user.universitydetails.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.unieval.R;
import com.example.unieval.data.pojo.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    List<Review> reviews;
    final private ListItemClickListener mOnclickListener;

    public ReviewAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(String reviewId);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if (reviews != null) {
            Review currentReview = reviews.get(position);
            Glide.with(holder.reviewImageView.getContext()).load(currentReview.getReviewerPhoto()).into(holder.reviewImageView);
            holder.reviewName.setText(currentReview.getReviewerName());
            holder.reviewClass.setText(currentReview.getReviewerClass());
            holder.reviewMessage.setText(currentReview.getReviewMessage());
            holder.reviewRating.setRating(currentReview.getReviewerRating());
        }
    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        } else {
            return 0;
        }
    }

    public void setList(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView reviewImageView;
        TextView reviewName, reviewClass, reviewMessage;
        RatingBar reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewImageView = itemView.findViewById(R.id.review_imageView);
            reviewName = itemView.findViewById(R.id.review_nameTextView);
            reviewClass = itemView.findViewById(R.id.review_classTextView);
            reviewMessage = itemView.findViewById(R.id.review_messageTextView);
            reviewRating = itemView.findViewById(R.id.review_ratingBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnclickListener.onListItemClick(reviews.get(clickedPosition).getReviewId());
                }
            });
        }
    }

}
