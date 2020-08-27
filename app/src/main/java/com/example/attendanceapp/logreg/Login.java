package com.example.attendanceapp.logreg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendanceapp.R;
import com.example.attendanceapp.Student;
import com.example.attendanceapp.Teacher;
import com.example.attendanceapp.model.Localuser;
import com.example.attendanceapp.model.Uid;
import com.example.attendanceapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private FirebaseDatabase database;
    User user=new User();
    Uid ud=new Uid();
    private TextView tforget;
    private EditText temail,tpass;
    private String email,pass;
    private ProgressBar tpb;
    private int flag=0;
    Localuser lu=new Localuser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        initView();

        tforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //forget password code here
                Toast.makeText(Login.this, "Under progress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView()
    {
        temail=findViewById(R.id.loginemail);
        tpass=findViewById(R.id.loginpass);
        tpb=findViewById(R.id.pb);
        tforget=findViewById(R.id.forgetpass);
    }

    public void login(View view)
    {
        tpb.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        if (valid())
        {
            fetchUser();
        }
        else
        {
        }
    }

    private void fetchUser()
    {
        database.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tpb.setVisibility(View.GONE);

                Iterable<DataSnapshot> it= dataSnapshot.getChildren();
                Iterator<DataSnapshot> dsit =it.iterator();
                while(dsit.hasNext())
                {
                    DataSnapshot ds=dsit.next();
                    //ds.getKey();  to get the user key
                    //Object o=ds.getValue();
                    user=ds.getValue(User.class);
                    //Toast.makeText(login.this, "Name="+user.getName(), Toast.LENGTH_SHORT).show();
                    if (email.equals(user.getEmail()))
                    {
                        flag=1;
                        //Toast.makeText(login.this, "Name="+user.getName()+"\n Role="+user.getRole(), Toast.LENGTH_SHORT).show();
                        if (pass.equals(user.getPass()))
                        {
                            //Toast.makeText(login.this, "Welcome"+user.getName()+"\n Role= "+user.getRole(), Toast.LENGTH_SHORT).show();
                            if (user.getRole().equalsIgnoreCase("teacher"))
                            {
                                //Toast.makeText(Login.this, "Welcome Teacher", Toast.LENGTH_SHORT).show();
                                ud.setUid(ds.getKey());
                                lu.setEmail(user.getEmail());
                                lu.setPass(user.getPass());
                                lu.setRole(user.getRole());
                                Session();
                                teacher();
                            }
                            else if (user.getRole().equalsIgnoreCase("student"))
                            {
                                //Toast.makeText(login.this, "Welcome Student", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(login.this, "Loginpage "+ds.getKey(), Toast.LENGTH_SHORT).show();
                                ud.setUid(ds.getKey());
                                lu.setEmail(user.getEmail());
                                lu.setPass(user.getPass());
                                lu.setRole(user.getRole());
                                Session();
                                student();
                            }
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if (flag==0)
                {
                    Toast.makeText(Login.this, "User not exist", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tpb.setVisibility(View.GONE);
            }
        });
    }

    private void teacher()
    {
        Intent in=new Intent(this, Teacher.class);
        startActivity(in);
        finish();
    }

    private void Session()
    {
            SharedPreferences sp=getSharedPreferences("Test",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.putString("email",lu.getEmail());
            e.putString("pass",lu.getPass());
            e.putString("role",lu.getRole());
            e.putString("uid",ud.getUid());
            e.commit();

    }

    private void student()
    {
        Intent in=new Intent(this, Student.class);
        startActivity(in);
        finish();
    }

    private String emailPattern="^[a-zA-Z0-9]{1,20}@[a-zA-Z]{1,20}.[a-zA-Z]{2,3}$";

    private boolean valid()
    {

        email=temail.getText().toString();
        pass=tpass.getText().toString();
        if (!(TextUtils.isEmpty(email)) && isEmailValidated(email))
        {
            if (!(TextUtils.isEmpty(pass)))
            {
                return true;
            }
            else
            {
                tpb.setVisibility(View.GONE);
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                return false;

            }
        }
        else
        {
            tpb.setVisibility(View.GONE);
            Toast.makeText(this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            temail.requestFocus();
            return false;

        }
    }

    private boolean isEmailValidated(String email)  //validating email
    {
        Pattern pattern=Pattern.compile(emailPattern);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

    public void signin(View view) {
        //Toast.makeText(this, "Create account", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Register.class));
    }
}