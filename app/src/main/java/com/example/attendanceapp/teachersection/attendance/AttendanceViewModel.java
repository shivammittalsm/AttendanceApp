package com.example.attendanceapp.teachersection.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AttendanceViewModel() {
        mText = new MutableLiveData<>();
      //  mText.setValue("This is Teacher Attendance fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}