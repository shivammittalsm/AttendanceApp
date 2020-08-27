package com.example.attendanceapp.model;

public class AttendanceList
{
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private boolean status;
    private String Name;
    private String collegeId;
}
