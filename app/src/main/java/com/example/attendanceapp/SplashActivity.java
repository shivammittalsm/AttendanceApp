package com.example.attendanceapp;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.attendanceapp.logreg.Login;
import com.example.attendanceapp.model.Uid;

public class SplashActivity extends AppCompatActivity {
    private LottieAnimationView anim;
    Uid ud=new Uid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        move();
    }
    private void move()
    {
        anim=findViewById(R.id.myanimationview);
        anim.addAnimatorListener(new  Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Intent in=new Intent(SplashActivity.this, login.class);
                //startActivity(in);
                //finish();
                SharedPreferences sp=getSharedPreferences("Test",MODE_PRIVATE);
                String email=sp.getString("email",null);
                String pass=sp.getString("pass",null);
                String role=sp.getString("role",null);
                String uid=sp.getString("uid",null);
                ud.setUid(uid);
                //Toast.makeText(SplashActivity.this, "UID="+uid, Toast.LENGTH_SHORT).show();
                if (email!=null && pass!=null)
                {
                    if (role.equalsIgnoreCase("Student"))
                    {
                        student();
                    }
                    else if (role.equalsIgnoreCase("Teacher"))
                    {
                        teacher();
                    }
                    else
                    {
                        Toast.makeText(SplashActivity.this, "Some Error occured", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    login();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void teacher()
    {
        //Toast.makeText(this, "Teacher Page", Toast.LENGTH_SHORT).show();
        Intent in=new Intent(this,Teacher.class);
        startActivity(in);
        finish();
    }

    private void student()
    {
        Intent in=new Intent(this,Student.class);
        startActivity(in);
        finish();
    }

    private void login()
    {
        Intent in=new Intent(this, Login.class);
        startActivity(in);
        finish();
    }
}
