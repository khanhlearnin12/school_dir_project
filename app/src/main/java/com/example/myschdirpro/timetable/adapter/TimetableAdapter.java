package com.example.myschdirpro.timetable.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private List<Course> list = new ArrayList<>();
    private OnCourseDeleteListener deleteListener;
    public void setOnCourseDeleteListener(OnCourseDeleteListener listener) {
        this.deleteListener = listener;
    }
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

        Log.d("TT_BIND", "bind pos=" + position +
                " name=" + c.name +
                " room=" + c.room +
                " time=" + c.time);

        holder.time.setText(c.time);


        if (holder.nameWatcher != null) {
            holder.etName.removeTextChangedListener(holder.nameWatcher);
        }
        if (holder.roomWatcher != null) {
            holder.etRoom.removeTextChangedListener(holder.roomWatcher);
        }
        if (holder.timeWatcher != null) {
            holder.etTime.removeTextChangedListener(holder.timeWatcher);
        }

        if (editMode) {

            holder.name.setVisibility(View.GONE);
            holder.room.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);

            holder.etName.setVisibility(View.VISIBLE);
            holder.etRoom.setVisibility(View.VISIBLE);
            holder.etTime.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);


            holder.etName.setText(c.name);
            holder.etRoom.setText(c.room);
            holder.etTime.setText(c.time);

            holder.etName.clearFocus();
            holder.etRoom.clearFocus();
            holder.etTime.clearFocus();

            holder.nameWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    c.name = s.toString();
                }
            };

            holder.roomWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    c.room = s.toString();
                }
            };

            holder.timeWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {c.time = s.toString();}
            };

            holder.etName.addTextChangedListener(holder.nameWatcher);
            holder.etRoom.addTextChangedListener(holder.roomWatcher);
            holder.etTime.addTextChangedListener(holder.timeWatcher);

            holder.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        deleteListener.onDelete(pos);
                    }
                }
            });

        } else {

            holder.name.setVisibility(View.VISIBLE);
            holder.room.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);

            holder.etName.setVisibility(View.GONE);
            holder.etRoom.setVisibility(View.GONE);
            holder.etTime.setVisibility(View.GONE);

            holder.name.setText(c.name);
            holder.room.setText(c.room);
            holder.time.setText(c.time);

            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //編輯模式
    private boolean editMode = false;
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public List<Course> getData() {
        return list;
    }

    public void addItem() {
        list.add(new Course("", "", ""));
        notifyItemInserted(list.size() - 1);
    }
    public void removeItem(int position) {

        Log.d("TT_DELETE", "REMOVE start size = " + list.size());
        Log.d("TT_DELETE", "REMOVE pos = " + position);

        if (position < 0 || position >= list.size()) {
            Log.d("TT_DELETE", "INVALID POSITION");
            return;
        }

        list.remove(position);

        notifyItemRemoved(position);

        Log.d("TT_DELETE", "REMOVE success size = " + list.size());

        notifyItemRangeChanged(position, list.size() - position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, name, room;
        EditText etRoom,etName,etTime;
        TextWatcher nameWatcher,roomWatcher,timeWatcher;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.tv_course_Time);
            name = itemView.findViewById(R.id.tv_course_CourseName);
            room = itemView.findViewById(R.id.tv_course_Room);

            etName = itemView.findViewById(R.id.et_course_CourseName);
            etRoom = itemView.findViewById(R.id.et_course_Room);
            etTime = itemView.findViewById(R.id.et_course_Time);

            btnDelete = itemView.findViewById(R.id.btn_course_delete);
        }
    }
}
