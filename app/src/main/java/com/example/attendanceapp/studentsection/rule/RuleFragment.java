package com.example.attendanceapp.studentsection.rule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendanceapp.R;

public class RuleFragment extends Fragment {
   private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_student_rule, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          WebView web=root.findViewById(R.id.myWebView);
          String url="file:///android_asset/Studentrules.html";
          web.loadUrl(url);
          web.clearCache(true);
          web.getSettings().setJavaScriptEnabled(true);
    }
}