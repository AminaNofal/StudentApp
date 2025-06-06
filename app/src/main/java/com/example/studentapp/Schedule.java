package com.example.studentapp;

public class Schedule {
    String subjectName, day, time, location;

    public Schedule(String subjectName, String day, String time, String location) {
        this.subjectName = subjectName;
        this.day = day;
        this.time = time;
        this.location = location;
    }

    public String getSubjectName() { return subjectName; }
    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
}
