package com.example.unieval.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unieval.R;
import com.example.unieval.data.pojo.EvaluationSummary;

import java.util.List;

public class EvaluationSummaryAdapter extends RecyclerView.Adapter<EvaluationSummaryAdapter.EvaluationSummaryViewHolder> {

    List<EvaluationSummary> evaluationSummaries;


    @NonNull
    @Override
    public EvaluationSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_criteria_report, parent, false);
        return new EvaluationSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluationSummaryViewHolder holder, int position) {
        if (evaluationSummaries != null) {
            EvaluationSummary currentEvaluationSummary = evaluationSummaries.get(position);
            holder.criteria.setText(currentEvaluationSummary.getCriteria());
            holder.userValue.setText(currentEvaluationSummary.getUserValue());
            holder.dbValue.setText(currentEvaluationSummary.getDbValue());
            if (currentEvaluationSummary.isCriteriaMet()) {
                holder.verdict.setImageResource(R.drawable.baseline_verified_24);
            } else {
                holder.verdict.setImageResource(R.drawable.baseline_new_releases_24);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (evaluationSummaries != null) {
            return evaluationSummaries.size();
        } else {
            return 0;
        }
    }

    public void setList(List<EvaluationSummary> evaluationSummaries) {
        this.evaluationSummaries = evaluationSummaries;
        notifyDataSetChanged();
    }

    class EvaluationSummaryViewHolder extends RecyclerView.ViewHolder {

        ImageView verdict;
        TextView criteria, userValue, dbValue;

        public EvaluationSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            verdict = itemView.findViewById(R.id.imageView5);
            criteria = itemView.findViewById(R.id.textView10);
            userValue = itemView.findViewById(R.id.textView26);
            dbValue = itemView.findViewById(R.id.textView27);

        }
    }
}
