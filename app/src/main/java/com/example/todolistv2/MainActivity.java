package com.example.todolistv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskCompleted(Task task, boolean isCompleted) {
                task.setCompleted(isCompleted);
                taskViewModel.update(task);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTaskDeleted(Task task) {
                taskList.remove(task);
                taskViewModel.delete(task);
                taskAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(taskAdapter);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskList.clear();
            taskList.addAll(tasks);
            taskAdapter.notifyDataSetChanged();
        });

        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(v -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Додати завдання");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);

        final EditText inputTitle = dialogView.findViewById(R.id.inputTitle);
        final EditText inputDescription = dialogView.findViewById(R.id.inputDescription);

        builder.setPositiveButton("Додати", (dialog, which) -> {
            String title = inputTitle.getText().toString();
            String description = inputDescription.getText().toString();

            if (!title.isEmpty()) {
                Task newTask = new Task(title, description, false);
                taskViewModel.insert(newTask);
            }
        });

        builder.setNegativeButton("Відмінити", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}