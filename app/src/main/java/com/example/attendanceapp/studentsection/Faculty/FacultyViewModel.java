package com.example.attendanceapp.studentsection.Faculty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FacultyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FacultyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is fragment for faculty list");
    }

    public LiveData<String> getText() {
        return mText;
    }
}