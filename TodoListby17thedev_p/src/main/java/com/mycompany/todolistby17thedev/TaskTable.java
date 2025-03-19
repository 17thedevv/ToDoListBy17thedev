/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.todolistby17thedev;

import java.util.List;
/**
 *
 * @author Asus
 */
import javax.swing.table.DefaultTableModel;
public class TaskTable extends DefaultTableModel {
    
    public TaskTable()
    {
        super(Global.COLUMNS, 0);
    }

    public void initializeTable(List<Task> taskList) {
        setRowCount(0); // Clear all rows
        for (Task task : taskList) {
            addRow(new Object[]{
                task.getId(),
                task.getTaskName(),
                task.getStartTime(),
                task.getDeadline(),
                task.isCompleted(),
                task.getPoints()
            });
        }
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4; // Only allow editing in the "Completed" column (if needed)
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 4) {
            return Boolean.class; // Makes the "Completed" column use checkboxes
        }
        return super.getColumnClass(columnIndex);
    }
    public void addTask(Task task) {
        addRow(new Object[]{ // Use addRow with a proper Object array
            task.getId(),
            task.getTaskName(),
            task.getStartTime(),
            task.getDeadline(),
            task.isCompleted(),
            task.getPoints()
        });
    }    
    public void updateTable(List<Task> taskList) {
        setRowCount(0); // Xóa toàn bộ dữ liệu hiện tại trong bảng
        for (Task task : taskList) {
            addTask(task); // Thêm lại các task còn lại vào bảng
        }
    }
    
}
