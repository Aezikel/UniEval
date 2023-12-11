package com.example.unieval.ui.admin.manageuniversity.research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unieval.R;
import com.example.unieval.data.pojo.Research;

import java.util.List;

public class UniversityResearchAdapter extends RecyclerView.Adapter<UniversityResearchAdapter.UniversityResearchViewHolder> {


    final private ListItemClickListener mOnclickListener;
    List<Research> research;

    public UniversityResearchAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public UniversityResearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_courses, parent, false);
        return new UniversityResearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityResearchViewHolder holder, int position) {
        if (research != null) {
            Research currentResearch = research.get(position);
            holder.courseTitle.setText(currentResearch.getResearchTitle());
            holder.coursePrice.setText(currentResearch.getResearchSalary());
        }

    }

    @Override
    public int getItemCount() {
        if (research != null) {
            return research.size();
        } else {
            return 0;
        }
    }

    public void setList(List<Research> research) {
        this.research = research;
        notifyDataSetChanged();
    }

    public Research getResearch(int position) {
        return research.get(position);
    }

    public void remove(int position) {
        research.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public PopupMenu createPopupMenu(ImageView imageView) {
        //Creating the instance of PopupMenu
        PopupMenu popupMenu = new PopupMenu(imageView.getContext(), imageView);
        //Inflating menu resource
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_course, popupMenu.getMenu());
        popupMenu.show();
        return popupMenu;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedPosition, ImageView menu);
    }

    class UniversityResearchViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle, coursePrice;
        ImageView menu;
        public UniversityResearchViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.courseTitle_textView);
            coursePrice = itemView.findViewById(R.id.coursePrice_textView);
            menu = itemView.findViewById(R.id.courseMenu_Icon);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnclickListener.onListItemClick(clickedPosition, menu);
                }
            });
        }

    }

}
