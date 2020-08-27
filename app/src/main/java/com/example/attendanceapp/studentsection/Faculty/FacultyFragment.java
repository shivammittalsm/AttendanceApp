package com.example.attendanceapp.studentsection.Faculty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapter.UserAdapter;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.model.UsersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class FacultyFragment extends Fragment {
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    User user=new User();
    private ProgressBar pb;
    ArrayList<UsersList> list=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_faculty, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database=FirebaseDatabase.getInstance();
        recyclerView =view.findViewById(R.id.userRecyclerView1);
        pb=view.findViewById(R.id.progress_bar);
        pb.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        makeList();
    }

    private void makeList()
    {
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> it=snapshot.getChildren();
                Iterator<DataSnapshot> dsit=it.iterator();
                while (dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    user=ds.getValue(User.class);
                    if (user.getRole().equalsIgnoreCase("teacher"))
                    {
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

    private void sendList()
    {
        pb.setVisibility(View.GONE);
        UserAdapter userAdapter=new UserAdapter(list);
        recyclerView.setAdapter(userAdapter);
    }

    private void addUser()
    {
        UsersList usersList=new UsersList();
        usersList.setName(user.getName());
        usersList.setEmail(user.getEmail());
        list.add(usersList);
    }
}