package com.example.attendanceapp.logreg;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String item;
    private EditText tname,tmobile,temail,tpass,tcpass,tid;
    private String name,mobile,email,pass,cpass,id;
    private FirebaseDatabase database;
    private ProgressBar tpb;
    User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Teacher");
        categories.add("Student");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        //Toast.makeText(this, ""+item, Toast.LENGTH_SHORT).show();
        initview();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO Auto-generated method stub
    }

    private void initview()
    {
        tname=findViewById(R.id.regname);
        tmobile=findViewById(R.id.regmobile);
        tid=findViewById(R.id.regid);
        temail=findViewById(R.id.regemail);
        tpass=findViewById(R.id.regpass);
        tcpass=findViewById(R.id.regpass1);
        tpb=findViewById(R.id.pb);
    }

    public void submit(View view) {
        //Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
        if (valid())
        {
            tpb.setVisibility(View.VISIBLE);
           // Log.e("error",user.getName()+"\n"+user.getMobile()+"\n"+user.getRole()+"\n"+user.getEmail()+"\n"+user.getPass()+"\n"+user.getId());
            sendDataToFCM();
        }
        else
        {

        }
    }

    private void sendDataToFCM()
    {
        database.getReference("users").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    tpb.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                }
                else
                {
                    tpb.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Registration Fail \n Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void clearAll()
    {
        tname.setText("");
        tmobile.setText("");
        tid.setText("");
        temail.setText("");
        tpass.setText("");
        tcpass.setText("");
    }

    private boolean valid()
    {
        name=tname.getText().toString();
        mobile=tmobile.getText().toString();
        id=tid.getText().toString();
        email=temail.getText().toString();
        pass=tpass.getText().toString();
        cpass=tcpass.getText().toString();

        if(!(TextUtils.isEmpty(name)) && isNameValidated(name))
        {
            if (!(TextUtils.isEmpty(mobile)) && isMobileValidated(mobile))
            {
                if (!(TextUtils.isEmpty(id)))
                {
                    if (!(item.equalsIgnoreCase("select")))
                    {
                        if (!(TextUtils.isEmpty(email)) && isEmailValidated(email))
                        {
                            if (!(TextUtils.isEmpty(pass)) && isPassValidated(pass))
                            {
                                if (!(TextUtils.isEmpty(cpass)) && isPassValidated(cpass))
                                {
                                    if (pass.equals(cpass)) {
                                        user.setName(name);
                                        user.setMobile(mobile);
                                        user.setId(id);
                                        user.setRole(item);
                                        user.setEmail(email);
                                        user.setPass(pass);
                                        return true;
                                    } else {
                                        Toast.makeText(this, "Please enter same password in both fields ", Toast.LENGTH_SHORT).show();
                                        clear();
                                        tpass.requestFocus();
                                        return false;
                                    }
                                } else {
                                    Toast.makeText(this, "Please Reenter Password", Toast.LENGTH_SHORT).show();
                                    tcpass.requestFocus();
                                    return false;
                                }
                            } else {
                                Toast.makeText(this, "Enter Valid Password \n password should have \n 1.one upper case \n 2.one lower case \n 3.one digit \n 4. one special char \n  5. Length greater than 6 digits", Toast.LENGTH_SHORT).show();
                                tpass.requestFocus();
                                return false;
                            }
                        } else {
                            Toast.makeText(this, "Enter Valid Email Id", Toast.LENGTH_LONG).show();
                            temail.requestFocus();
                            return false;
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "please select whether you are register as Student or as a teacher", Toast.LENGTH_SHORT).show();
                        Spinner sp=findViewById(R.id.spinner);
                        sp.requestFocus();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(this, "Enter college Id", Toast.LENGTH_SHORT).show();
                    tid.requestFocus();
                    return false;
                }
            }
            else
            {
                Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                tmobile.requestFocus();
                return false;
            }
        }
        else
        {
            Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show();
            tname.requestFocus();
            return false;
        }

    }

    private void clear()
    {
        tpass.setText("");
        tcpass.setText("");
    }

    private String fullName="^[a-zA-Z\\s]+";
    private String emailPattern="^[a-zA-Z0-9]{1,20}@[a-zA-Z]{1,20}.[a-zA-Z]{2,3}$";   //validation string for email
    private String passwordPattern="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!?&()@$%#]).{6,15})"; //validation string for password
    private String mobilePattern="\\d{10}";

    private boolean isNameValidated(String name)
    {
        Pattern pattern=Pattern.compile(fullName);
        Matcher matcher=pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isEmailValidated(String email)  //validating email
    {
        Pattern pattern=Pattern.compile(emailPattern);
        Matcher matcher=pattern.matcher(email);
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



    public void signin(View view) {
        //Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,Login.class));
        finish();
    }
}