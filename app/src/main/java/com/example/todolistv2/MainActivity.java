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

public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final TaskAdapter adapter = new TaskAdapter(new TaskAdapter.TaskDiff(), new TaskAdapter.OnTaskInteractionListener() {
            @Override
            public void onTaskCompleted(Task task, boolean isCompleted) {
                task.setCompleted(isCompleted);
                taskViewModel.update(task);
            }

            @Override
            public void onTaskDeleted(Task task) {
                taskViewModel.delete(task);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
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

