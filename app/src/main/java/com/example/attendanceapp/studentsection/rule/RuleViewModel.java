package com.example.attendanceapp.studentsection.rule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RuleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RuleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Rule fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}