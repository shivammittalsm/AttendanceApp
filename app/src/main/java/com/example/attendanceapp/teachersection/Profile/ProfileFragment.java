package com.example.attendanceapp.teachersection.Profile;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Uid;
import com.example.attendanceapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ProfileFragment extends Fragment {
    private EditText tname,temail,tmobile,tcid;
    private ProgressBar pb;
    private Button button;
    private View view;
    Uid uid=new Uid();
    User user=new User();
    private FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        database = FirebaseDatabase.getInstance();
        initView();
        getDataFromFcm();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });
    }

    private void getDataFromFcm()
    {
        pb.setVisibility(View.VISIBLE);
        final String userkey=uid.getUid();
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> it=snapshot.getChildren();
                Iterator<DataSnapshot> dsit=it.iterator();
                while (dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    if (ds.getKey().equalsIgnoreCase(userkey))
                    {
                        user=ds.getValue(User.class);
                        pb.setVisibility(View.GONE);
                        showData();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "some error occured", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void showData()
    {
        tname.setText(user.getName());
        temail.setText(user.getEmail());
        tmobile.setText(user.getMobile());
        tcid.setText(user.getId());
    }

    private void updateDetails()
    {
        Toast.makeText(getActivity(), "Under Progress", Toast.LENGTH_SHORT).show();
    }

    private void initView()
    {
        tname=view.findViewById(R.id.tname);
        temail=view.findViewById(R.id.temail);
        tmobile=view.findViewById(R.id.tmobiile);
        tcid=view.findViewById(R.id.tid);
        button=view.findViewById(R.id.tupdate);
        pb=view.findViewById(R.id.tbar);
    }
}