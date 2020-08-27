package com.example.attendanceapp.studentsection.attendance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.AttendanceList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class AttendanceFragment extends Fragment {
    private Button searchButton;
    private EditText tgetDate,tname,tid,tstatus,tgetId;
    private FirebaseDatabase database;
    private String date,id;
    private AttendanceList attendanceList;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_attendance, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchButton=view.findViewById(R.id.studentSearchButton);
        tgetDate=view.findViewById(R.id.studentSearchdate);
        tgetId=view.findViewById(R.id.studentSearchId);
        database=FirebaseDatabase.getInstance();
        tname=view.findViewById(R.id.showName);
        tid=view.findViewById(R.id.showId);
        tstatus=view.findViewById(R.id.showStatus);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid())
                {
                    final int[] flag = {0};
                    database.getReference("Attendance").child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> it=snapshot.getChildren();
                            Iterator<DataSnapshot> dsit =it.iterator();
                            while (dsit.hasNext())
                            {
                                DataSnapshot ds=dsit.next();
                                attendanceList=ds.getValue(AttendanceList.class);
                                //Log.e("erroe",attendanceList.getName());
                                if (id.equalsIgnoreCase(attendanceList.getCollegeId()))
                                {
                                    flag[0]++;
                                    tname.setText(attendanceList.getName());
                                    tid.setText(attendanceList.getCollegeId());
                                    if(attendanceList.isStatus())
                                    {
                                        tstatus.setText("Present");
                                    }
                                    else
                                    {
                                        tstatus.setText("Absent");
                                    }
                                }
                            }
                            if (flag[0]==0)
                            {
                                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }



    private static final String DATE_PATTERN = "^[0-9]{4}-(0[1-9]|1[0-2])-(1[0-9]|0[1-9]|3[0-1]|2[1-9])$";

    private boolean isDateValidated(String date)
    {
        Pattern pattern=Pattern.compile(DATE_PATTERN);
        Matcher matcher=pattern.matcher(date);
        return matcher.matches();
    }

    private boolean valid()
    {
        String date=tgetDate.getText().toString();
        String id=tgetId.getText().toString();

        if (!(date.isEmpty()))
        {
            if (isDateValidated(date))
            {
                this.date=date;
                if (!(id.isEmpty()))
                {
                    this.id=id;
                    return true;
                }
                else
                {
                    Toast.makeText(getActivity(), "Enter Your College Id", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Please Enter Date in yyyy-mm-dd format", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Please Enter Date", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}