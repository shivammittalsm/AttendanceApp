package com.example.attendanceapp.teachersection.attendance;

import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapter.AttendanceAdapter;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.AttendanceList;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.model.UsersList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class AttendanceFragment extends Fragment {
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private ProgressBar pb;
    private AttendanceAdapter attendanceAdapter;
    private Button selectAll,submit;
    User user=new User();
    ArrayList<AttendanceList> list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_attendance, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database=FirebaseDatabase.getInstance();
        recyclerView=view.findViewById(R.id.attendanceRecyclerView);
        pb=view.findViewById(R.id.pb);
        selectAll=view.findViewById(R.id.selectall);
        submit=view.findViewById(R.id.submit);

        pb.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        makeList();

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAll.getText().toString().equalsIgnoreCase("select all"))
                {
                    selectAll.setText("Deselect All");
                    for (AttendanceList s:list)
                    {
                        s.setStatus(true);
                    }
                }
                else if (selectAll.getText().toString().equalsIgnoreCase("Deselect All"))
                {
                    selectAll.setText("Select All");
                    for (AttendanceList s:list)
                    {
                        s.setStatus(false);
                    }
                }
                //recyclerView.setAdapter(new AttendanceAdapter(list));
                attendanceAdapter.notifyDataSetChanged();
            }
        });

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendance").child(date);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Date="+date, Toast.LENGTH_SHORT).show();
                final int[] done = {0};
                for (AttendanceList attendanceList:list)
                {
                    ref.child(attendanceList.getCollegeId()).setValue(attendanceList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!(task.isSuccessful()))
                            {
                                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                                done[0]++;
                            }
                        }
                    });
                }
               if (done[0]==0)
               {
                   Toast.makeText(getActivity(), "Attendance marked successfully", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }



    private void makeList()
    {
        list=new ArrayList<>();
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> it=snapshot.getChildren();
                Iterator<DataSnapshot> dsit=it.iterator();
                while (dsit.hasNext())
                {
                    DataSnapshot ds =dsit.next();
                    user=ds.getValue(User.class);
                    if (user.getRole().equalsIgnoreCase("student"))
                    {
                        addUser();
                    }
                }
                sendList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pb.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendList()
    {
        pb.setVisibility(View.GONE);
        attendanceAdapter=new AttendanceAdapter(list);
        recyclerView.setAdapter(attendanceAdapter);
    }

    private void addUser()
    {
        AttendanceList attendanceList=new AttendanceList();
        attendanceList.setName(user.getName());
        attendanceList.setCollegeId(user.getId());
        list.add(attendanceList);
    }
}