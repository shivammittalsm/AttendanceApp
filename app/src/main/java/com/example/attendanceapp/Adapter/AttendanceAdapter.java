package com.example.attendanceapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.AttendanceList;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyView>
{
    private List<AttendanceList> list;

    public AttendanceAdapter(List<AttendanceList> list)
    {
        this.list=list;
    }
    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.attend_list,parent,false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        final AttendanceList attendanceList=list.get(position);
        holder.name.setText(attendanceList.getName());
        holder.roll.setText(attendanceList.getCollegeId());
        holder.tcheckBox.setChecked(attendanceList.isStatus());
        holder.tcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendanceList.setStatus(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyView extends RecyclerView.ViewHolder
    {
        private EditText name,roll;
        private CheckBox tcheckBox;
        public MyView(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.attend_name);
            roll=itemView.findViewById(R.id.attend_roll);
            tcheckBox=itemView.findViewById(R.id.myCheckbox);
        }
    }

}
