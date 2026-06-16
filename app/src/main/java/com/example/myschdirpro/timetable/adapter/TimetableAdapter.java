package com.example.myschdirpro.timetable.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.Course;

import java.util.ArrayList;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private List<Course> list = new ArrayList<>();

    public void updateData(List<Course> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course c = list.get(position);

        holder.time.setText(c.time);
        holder.name.setText(c.name);
        holder.room.setText(c.room);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, name, room;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_course_Time);
            name = itemView.findViewById(R.id.tv_course_CourseName);
            room = itemView.findViewById(R.id.tv_course_Room);
        }
    }
}
