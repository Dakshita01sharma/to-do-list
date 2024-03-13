import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
class Task implements Serializable {
    private String description;
    private boolean completed;
    private String dueDate;

    public Task(String description, String dueDate) {
        this.description = description;
        this.completed = false;
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDueDate() {
        return dueDate;
    }
}

class TodoListApp extends JFrame {
    private List<Task> tasks;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    public TodoListApp() {
        super("Todo List App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        initializeUI();
        loadTasks();
    }

    private void initializeUI() {
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton completeButton = new JButton("Complete Task");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeTask();
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(completeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog(this, "Enter task description:");
        String dueDate = JOptionPane.showInputDialog(this, "Enter due date (optional):");

        Task task = new Task(description, dueDate);
        tasks.add(task);
        updateTaskList();
        saveTasks();
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            tasks.remove(selectedIndex);
            updateTaskList();
            saveTasks();
        }
    }

    private void completeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task task = tasks.get(selectedIndex);
            task.setCompleted(true);
            updateTaskList();
            saveTasks();
        }
    }

    private void updateTaskList() {
        listModel.clear();
        for (Task task : tasks) {
            String status = task.isCompleted() ? "[Completed] " : "[Pending] ";
            listModel.addElement(status + task.getDescription() + " (Due: " + task.getDueDate() + ")");
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.ser"))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                tasks = (List<Task>) obj;
                updateTaskList();
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TodoListApp().setVisible(true);
            }
        });
    }
}

