package com.example.attendanceapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.AttendanceList;

import java.util.List;

public class ShowReportAdapter extends RecyclerView.Adapter<ShowReportAdapter.MyView>
{
    private List<AttendanceList> list;

    public ShowReportAdapter(List<AttendanceList> list)
    {
        this.list=list;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list,parent,false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        final AttendanceList attendanceList=list.get(position);
        holder.name.setText(attendanceList.getName());
        holder.roll.setText(attendanceList.getCollegeId());
        if (attendanceList.isStatus())
        {
            holder.status.setText("Present");

        }
        else
        {
            holder.status.setText("Absent");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyView extends RecyclerView.ViewHolder
    {
        private EditText name,roll,status;
        public MyView(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.searchName);
            roll=itemView.findViewById(R.id.searchID);
            status=itemView.findViewById(R.id.searchStatus);
        }
    }
}
