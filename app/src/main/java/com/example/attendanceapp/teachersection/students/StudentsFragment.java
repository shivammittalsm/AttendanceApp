package com.example.attendanceapp.teachersection.students;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapter.UserAdapter;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.UsersList;
import com.example.attendanceapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class StudentsFragment extends Fragment {
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    User user=new User();
    private ProgressBar pb;
    ArrayList<UsersList> list=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_student, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database=FirebaseDatabase.getInstance();
        recyclerView=view.findViewById(R.id.userRecyclerView);
        pb=view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        makeList();
    }


    private void makeList()
    {
        //Toast.makeText(getActivity(), "Make list", Toast.LENGTH_SHORT).show();
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> it=snapshot.getChildren();
                Iterator<DataSnapshot> dsit=it.iterator();
                while (dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    user=ds.getValue(User.class);
                    if (user.getRole().equalsIgnoreCase("Student"))
                    {
                       // Toast.makeText(getActivity(), "UID="+ds.getKey(), Toast.LENGTH_SHORT).show();
                        addUser();
                    }
                }
                sendList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addUser()
    {
        //Toast.makeText(getActivity(), "adduser", Toast.LENGTH_SHORT).show();
        UsersList usersList=new UsersList();
        usersList.setName(user.getName());
        usersList.setEmail(user.getEmail());
        list.add(usersList);
        //Toast.makeText(getActivity(), ""+list.toString(), Toast.LENGTH_SHORT).show();
        //Log.e("error",list.toString());
    }

    private void sendList()
    {
        pb.setVisibility(View.GONE);
       // Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
        UserAdapter userAdapter=new UserAdapter(list);
        recyclerView.setAdapter(userAdapter);
        //Log.e("error1",list.toString());
    }
}