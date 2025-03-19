package com.mycompany.todolistby17thedev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class TodoListby17thedev extends JFrame {

    public static TodoListby17thedev instance;

    private JTextField taskField;
    private JTextField startTimeField;
    private JTextField deadlineField;
    private JTextField pointField;
    private JLabel pointsLabel;
    private JTable jTable = new JTable();
    private JButton deleteButton = new JButton("Xóa Task");
    private TaskTable taskTabel = new TaskTable();
    private List<Task> taskList = new ArrayList<>();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    private static String FILE_PATH = "C:\\Users\\84387\\Documents\\webcuatra\\TodoListby17thedev\\src\\main\\java\\tasks.txt";
    private int totalPoints = 0;

    public TodoListby17thedev() {
        setTitle("To-Do List by 17thedev");
        setIconImage(new ImageIcon("src\\resources\\hlinh.png").getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        InitComponent();
       
        OnApplicationOpen();
        InitTaskTable();
    }
    
    private void handleTaskCompletion(int taskId) {
        LocalDate today = LocalDate.now(); 
    
        for (Task task : taskList) {
            if (task.getId() == taskId) {
                // Kiểm tra nếu task đã quá hạn
                if (today.isAfter(task.getDeadline())) {
                    JOptionPane.showMessageDialog(this, "Task đã quá hạn chót và không thể hoàn thành!");
                    // Reset checkbox về trạng thái "chưa hoàn thành"
                    taskTabel.setValueAt(false, taskList.indexOf(task), 4);
                    return;
                }
    
                if (!task.isCompleted()) {
                    task.setCompleted(true); 
                    saveTasks();
                    setPoint(task.getPoints());
                    JOptionPane.showMessageDialog(this, "Task hoàn thành! +" + task.getPoints() + " điểm");
                }
                return;
            }
        }
    }
    
    public void setPoint(int point)
    {
        totalPoints += point;
        pointsLabel.setText("Điểm: " + totalPoints);
    }

    private void addTask() {
    try {
        String taskName = taskField.getText();
        String startTimeStr = startTimeField.getText();
        String deadlineStr = deadlineField.getText();      
        int point;
        try {
            point = Integer.parseInt(pointField.getText());
            if (point < 0) { // Optional: Check if points are non-negative
                JOptionPane.showMessageDialog(this, "Điểm phải là một số nguyên không âm.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập một số nguyên hợp lệ cho Điểm.");
            return;
        }

        if (!Controller.isValidDate(startTimeStr) || !Controller.isValidDate(deadlineStr)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu hoặc hạn chót không hợp lệ. Định dạng: YYYY-MM-DD");
            return;
        }

        // Parse dates
        LocalDate startTime = LocalDate.parse(startTimeStr);
        LocalDate deadline = LocalDate.parse(deadlineStr);
        
        Task newTask;
        
        if(taskList.size() != 0)
        {
            newTask = new Task(taskList.get(taskList.size() - 1).getId() + 1, taskName, startTime, false, deadline, point);
        }
        else
        {
            newTask = new Task(1, taskName, startTime, rootPaneCheckingEnabled, deadline, point);
        }
        taskList.add(newTask);
        taskTabel.addTask(newTask);
        saveTasks();
        JOptionPane.showMessageDialog(this, "Task đã được thêm!");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm task: " + e.getMessage());
    }
    }
    
    private void loadTasks() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            taskList = gson.fromJson(reader, new TypeToken<ArrayList<Task>>() {}.getType());
            if (taskList == null) {
                taskList = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            taskList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.google.gson.JsonSyntaxException e) {
            taskList = new ArrayList<>();
        }
    }

    private void saveTasks() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(taskList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void InitComponent()
    {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        taskField = new JTextField();
        startTimeField = new JTextField("YYYY-MM-DD");
        deadlineField = new JTextField("YYYY-MM-DD");
        pointField = new JTextField();

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Thời gian bắt đầu:"));
        inputPanel.add(startTimeField);
        inputPanel.add(new JLabel("Hạn chót:"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Điểm: "));
        inputPanel.add(pointField);

        JButton addButton = new JButton("Thêm Task");
        inputPanel.add(addButton);

        pointsLabel = new JLabel("Điểm: 0");
        inputPanel.add(pointsLabel);

        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);
        jTable.setModel(taskTabel);
        jTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScrollPane = new JScrollPane(jTable);
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> {
            String[] options = {"Xóa theo ID", "Xóa theo Điểm"};
            int choice = JOptionPane.showOptionDialog(
                tableScrollPane,
                "Bạn muốn xóa theo cách nào?",
                "Xóa Task",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
        
            if (choice == 0) { 
                int ID;
                try {
                    ID = Integer.parseInt(JOptionPane.showInputDialog(tableScrollPane,"Nhập ID: "));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tableScrollPane, "Bạn chưa nhập ID");
                    return;
                }
                DeleteTaskbyID(ID);
            } else if (choice == 1) { // Xóa theo Điểm
                int point = Integer.parseInt(JOptionPane.showInputDialog(tableScrollPane,"Nhập Điểm: "));
                DeleteTaskbyPoint(point);
            }
        });
        
        
        
        add(tableScrollPane, BorderLayout.CENTER);
    }

    public void InitTaskTable()
    {
        taskTabel.addTableModelListener(e -> {
            int row = e.getFirstRow(); // Dòng vừa được thay đổi
            int column = e.getColumn(); // Cột vừa được thay đổi
        
            if (column == 4) { // Chỉ xử lý các thay đổi trong cột "Completed"
                Boolean isCompleted = (Boolean) taskTabel.getValueAt(row, column); // Lấy giá trị checkbox
                int taskId = (Integer) taskTabel.getValueAt(row, 0); // Lấy ID của task từ cột đầu tiên
        
                // Xử lý sự kiện khi checkbox được nhấp
                if (isCompleted) {
                    handleTaskCompletion(taskId); // Gọi hàm xử lý hoàn thành task
                } else {
                    for (Task task : taskList) {
                        if(task.getId() == taskId)
                        {
                           task.setCompleted(false);
                           setPoint(-task.getPoints());
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Bạn đã bỏ chọn hoàn thành task.");
                }
            }
        });
        taskTabel.initializeTable(taskList);
    }
    
    public void DeleteTaskbyID(int ID) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == ID) {
                if(taskList.get(i).isCompleted())
                    setPoint(-taskList.get(i).getPoints()); 
                taskList.remove(i); 
                taskTabel.updateTable(taskList);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy task có ID này");
    }
    

    public void DeleteTaskbyPoint(int points) {
        int j = 0;
        for (int i = taskList.size() - 1; i >= 0; i--) { 
            if (taskList.get(i).getPoints() == points) {
                if(taskList.get(i).isCompleted())
                    setPoint(-points); 
                taskList.remove(i); 
                j++;
            }
        }
        if(j != 0)
          taskTabel.updateTable(taskList);
        else
          JOptionPane.showMessageDialog(this, "không có task nào có điểm:  " + points);
    }
    
    public void OnApplicationOpen()
    {
        loadTasks();
        for (Task task : taskList) {
            if(task.isCompleted())
            {
                setPoint(task.getPoints());
            }
        }
    }

    public void OnApplicationQuit()
    {
        saveTasks();
    }

    public static TodoListby17thedev getInstance()
    {
        if(instance == null)
           instance = new TodoListby17thedev();
        return instance;
    }
    public static void main(String args[])
    {
        TodoListby17thedev app = getInstance();
        app.setVisible(true);
        app.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                app.OnApplicationQuit();
            }
        });
    }
}
