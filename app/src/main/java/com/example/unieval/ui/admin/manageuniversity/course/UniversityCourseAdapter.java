package com.example.unieval.ui.admin.manageuniversity.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.unieval.R;
import com.example.unieval.data.pojo.Course;

import java.util.List;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


public class UniversityCourseAdapter extends RecyclerView.Adapter<UniversityCourseAdapter.UniversityCourseViewHolder> {

    final private ListItemClickListener mOnclickListener;
    List<Course> courses;

    public UniversityCourseAdapter(ListItemClickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @NonNull
    @Override
    public UniversityCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_courses, parent, false);
        return new UniversityCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityCourseViewHolder holder, int position) {
        if (courses != null) {

            Course course = courses.get(position);
            holder.courseTitle.setText(course.getCourseTitle());
            holder.coursePrice.setText(course.getCoursePrice());
        }

    }

    @Override
    public int getItemCount() {
        if (courses != null) {
            return courses.size();
        } else {
            return 0;
        }
    }

    public void setList(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }



    public Course getCourse(int position) {
        return courses.get(position);
    }

    public void remove(int position) {
        courses.remove(position);
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

    class UniversityCourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitle, coursePrice;
        ImageView menu;

        public UniversityCourseViewHolder(@NonNull View itemView) {
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
