package com.example.attendanceapp.teachersection.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapter.ModuleAdapter;
import com.example.attendanceapp.R;
import com.example.attendanceapp.logreg.Login;
import com.example.attendanceapp.model.Module;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toast.makeText(getActivity(), "Welcome Teacher", Toast.LENGTH_SHORT).show();
        recyclerView=view.findViewById(R.id.myRecyclerView);
        //if we want recycler view
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        //if we want linear layout
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dummyModule();
        ModuleAdapter moduleAdapter=new ModuleAdapter(list);

        moduleAdapter.setMyCustomListener(new ModuleAdapter.ItemClickEvent(){
            @Override
            public void clickOnItem(View view, int position) {
                String name=list.get(position).getName();
                NavController navController= Navigation.findNavController(view);
                if (name.equalsIgnoreCase("profile"))
                {
                    navController.navigate(R.id.action_nav_thome_to_nav_tprofile);
                }
                else if (name.equalsIgnoreCase("Attendance"))
                {
                    navController.navigate(R.id.action_nav_thome_to_nav_tattendance);
                }
                else if (name.equalsIgnoreCase("Students"))
                {
                    navController.navigate(R.id.action_nav_thome_to_nav_tstudent);
                }
                else if (name.equalsIgnoreCase("report"))
                {
                    navController.navigate(R.id.action_nav_thome_to_nav_treport);
                }
                else if (name.equalsIgnoreCase("logout"))
                {
                    SharedPreferences sp=getActivity().getSharedPreferences("Shiv",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove("email");
                    editor.remove("pass");
                    editor.remove("role");
                    editor.commit();
                    Intent in=new Intent(getActivity(), Login.class);
                    startActivity(in);
                    getActivity().finish();
                }
            }
        });

        recyclerView.setAdapter(moduleAdapter);
    }

    List<Module> list;

    private void dummyModule() {
        list= new ArrayList<>();

        Module m1 = new Module();
        m1.setName("Profile");
        m1.setImage(R.drawable.icon_profile);

        Module m2 = new Module();
        m2.setName("Attendance");
        m2.setImage(R.drawable.attendanceicon);

        Module m3 = new Module();
        m3.setName("Students");
        m3.setImage(R.drawable.studentlisticon);

        Module m4=new Module();
        m4.setName("Report");
        m4.setImage(R.drawable.report);

        Module m5 = new Module();
        m5.setName("Logout");
        m5.setImage(R.drawable.logouticon);

        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
    }

}