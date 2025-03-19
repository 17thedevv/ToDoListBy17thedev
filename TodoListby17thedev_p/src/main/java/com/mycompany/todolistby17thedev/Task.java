/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistby17thedev;

import java.time.LocalDate;

/**
 *
 * @author Asus
 */
public class Task {
    private int id;
    private String taskName;
    private LocalDate startTime;
    private LocalDate deadline;
    private boolean completed;
    private int points;

    public Task(int id, String taskName, LocalDate startTime, boolean isCompleted, LocalDate deadline, int points) {
        this.id = id;
        this.taskName = taskName;
        this.startTime = startTime;
        this.deadline = deadline;
        this.completed = isCompleted;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPoints() {
        return points;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
