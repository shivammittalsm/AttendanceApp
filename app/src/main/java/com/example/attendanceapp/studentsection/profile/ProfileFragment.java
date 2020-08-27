package com.example.attendanceapp.studentsection.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Uid;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.studentsection.updateDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ProfileFragment extends Fragment {
    Uid ud=new Uid();
    User user=new User();
    private FirebaseDatabase database;
    View view;
    private EditText tname,temail,tmobile,tuid;
    private ProgressBar pb;
    private Button update;
    private ProfileViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_home, container, false);
        database = FirebaseDatabase.getInstance();
        RelativeLayout ll=root.findViewById(R.id.relative);
        view=ll;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getDataFromFCM();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }


    private void updateData()
    {
        Intent in=new Intent(getActivity(), updateDetails.class);
        startActivity(in);
    }

    private void initView()
    {
        tname=view.findViewById(R.id.pname);
        temail=view.findViewById(R.id.pemail);
        tmobile=view.findViewById(R.id.pmobiile);
        tuid=view.findViewById(R.id.pid);
        update=view.findViewById(R.id.update);
        pb=view.findViewById(R.id.pbar);
    }

    private void showData()
    {
        tname.setText(user.getName());
        temail.setText(user.getEmail());
        tmobile.setText(user.getMobile());
        tuid.setText(user.getId());
    }

    private void getDataFromFCM()
    {
        pb.setVisibility(View.VISIBLE);
        final String userkey=ud.getUid();
        //Toast.makeText(getActivity(), "UID=="+ud.getUid(), Toast.LENGTH_SHORT).show();
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> it= dataSnapshot.getChildren();
                Iterator<DataSnapshot> dsit =it.iterator();
                while(dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    //ds.getKey();  to get the user key
                    if (ds.getKey().equalsIgnoreCase(userkey))
                    {
                        pb.setVisibility(View.GONE);
                        user=ds.getValue(User.class);
                        //Log.e("error","Token Match");
                        //Toast.makeText(getActivity(), "Name="+user.getName(), Toast.LENGTH_SHORT).show();
                        showData();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }
}