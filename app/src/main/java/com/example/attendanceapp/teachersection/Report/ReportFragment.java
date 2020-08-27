package com.example.attendanceapp.teachersection.Report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapter.ShowReportAdapter;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.AttendanceList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReportFragment extends Fragment {
    private EditText tdate;
    private Button tButton;
    private String date;
    private FirebaseDatabase database;
    private ProgressBar pb;
    AttendanceList attendanceList=new AttendanceList();
    private RecyclerView recyclerView;
    ArrayList<AttendanceList> list=new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_report, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tdate=view.findViewById(R.id.searchdate);
        recyclerView=view.findViewById(R.id.reportRecyclerView);
        pb=view.findViewById(R.id.progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tButton=view.findViewById(R.id.searchButton);
        database=FirebaseDatabase.getInstance();

        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                if (valid())
                {
                    //date=tdate.getText().toString();
                    //Toast.makeText(getActivity(), ""+date, Toast.LENGTH_SHORT).show();
                    database.getReference("Attendance").child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> it=snapshot.getChildren();
                            Iterator<DataSnapshot> dsit=it.iterator();
                            while (dsit.hasNext())
                            {
                                DataSnapshot ds=dsit.next();
                                attendanceList=ds.getValue(AttendanceList.class);
                                //Log.e("error",ds.getKey());
                                list.add(attendanceList);
                            }
                            sendList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private static final String DATE_PATTERN = "^[0-9]{4}-(0[1-9]|1[0-2])-(1[0-9]|0[1-9]|3[0-1]|2[1-9])$";

    private boolean valid()
    {
        String date=tdate.getText().toString();

        if (!(date.isEmpty()))
        {
            if (isDateValidated(date))
            {
                this.date=date;
                return true;
            }
            else
            {
                Toast.makeText(getActivity(), "Please Enter Date in yyyy-mm-dd format", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
                return false;
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Please Enter Valid Date", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            return false;
        }
    }

    private boolean isDateValidated(String date)
    {
        Pattern pattern=Pattern.compile(DATE_PATTERN);
        Matcher matcher=pattern.matcher(date);
        return matcher.matches();
    }

    private void sendList() {
        if (list.isEmpty())
        {
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
        }
        else
        {
            ShowReportAdapter showReportAdapter=new ShowReportAdapter(list);
            recyclerView.setAdapter(showReportAdapter);
            pb.setVisibility(View.GONE);
        }
    }
}