package com.example.studentapp;

public class Grade {
    private int id;
    private int studentId;
    private String subject;
    private float gradeValue;

    public Grade(int id, int studentId, String subject, float gradeValue) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.gradeValue = gradeValue;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public String getSubject() { return subject; }
    public float getGradeValue() { return gradeValue; }
}

