package com.example.attendanceapp.studentsection;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.model.Uid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class updateDetails extends AppCompatActivity {
    private EditText tname,tmobile,tpass,temail,tid;
    private String name,mobile,pass,email,id;
    private ProgressBar pb;
    User user=new User();
    Uid uid=new Uid();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        database = FirebaseDatabase.getInstance();
        initView();
    }

    private void initView()
    {
        tname=findViewById(R.id.uname);
        tmobile=findViewById(R.id.umobiile);
        tpass=findViewById(R.id.upass);
        temail=findViewById(R.id.uemail);
        tid=findViewById(R.id.uid);
        pb=findViewById(R.id.pb);
        getData();
    }

    private void getData()
    {
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> it= dataSnapshot.getChildren();
                Iterator<DataSnapshot> dsit =it.iterator();
                while(dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    if(ds.getKey().equalsIgnoreCase(uid.getUid()))
                    {
                        user=ds.getValue(User.class);
                        showData();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void showData()
    {
        tname.setText(user.getName());
        temail.setText(user.getEmail());
        tmobile.setText(user.getMobile());
        tid.setText(user.getId());
        tpass.setText(user.getPass());
        //Toast.makeText(this, ""+user.getName(), Toast.LENGTH_SHORT).show();
    }

    private String fullName="^[a-zA-Z\\s]+";
    private String passwordPattern="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!?&()@$%#]).{6,15})"; //validation string for password
    private String mobilePattern="\\d{10}";

    private boolean isNameValidated(String name)
    {
        Pattern pattern=Pattern.compile(fullName);
        Matcher matcher=pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isPassValidated(String pass)  //validating password
    {
        Pattern pattern=Pattern.compile(passwordPattern);
        Matcher matcher=pattern.matcher(pass);
        return matcher.matches();
    }

    private boolean isMobileValidated(String mobile) //validating mobile number
    {
        Pattern pattern= Pattern.compile(mobilePattern);
        Matcher matcher=pattern.matcher(mobile);
        return matcher.matches();
    }


    private void updateDataToFCM()
    {
        database.getReference("users").child(uid.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pb.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    //Toast.makeText(updateDetails.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Profile Updated", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getData();
                }
                else
                {
                    //Toast.makeText(updateDetails.this, "Unable to update profile", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Unable to update profile", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }


    public void updateDetails(View view)
    {
        pb.setVisibility(View.VISIBLE);
       if(valid())
       {
           //profile updation code here
           user.setName(name);
           user.setMobile(mobile);
           user.setPass(pass);
           user.setEmail(email);
           user.setId(id);
           updateDataToFCM();
       }
       else
       {
       }
    }

    private boolean valid()
    {
        name=tname.getText().toString();
        mobile=tmobile.getText().toString();
        pass=tpass.getText().toString();
        email=temail.getText().toString();
        id=tid.getText().toString();


        if(!(TextUtils.isEmpty(name)) && isNameValidated(name))
        {
            if (!(TextUtils.isEmpty(mobile)) && isMobileValidated(mobile))
            {
                if (!(TextUtils.isEmpty(pass)) && isPassValidated(pass))
                {
                    return true;
                }
                else
                {
                    Toast.makeText(this, "Enter Strong Password", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    tpass.requestFocus();
                    return false;
                }
            }
            else
            {
                Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                tmobile.requestFocus();
                return false;
            }
        }
        else
        {
            Toast.makeText(this, "Enter valid Name", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
            tname.requestFocus();
            return false;
        }
    }

}