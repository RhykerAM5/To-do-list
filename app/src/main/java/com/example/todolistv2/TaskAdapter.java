package com.example.todolistv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskClickListener listener;

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitle;
        private TextView taskDescription;
        private CheckBox checkBoxCompleted;
        private Button buttonDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(Task task) {
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());

            // Отключаем слушатель перед обновлением состояния, чтобы избежать неправильных вызовов
            checkBoxCompleted.setOnCheckedChangeListener(null);

            // Устанавливаем актуальное состояние чекбокса
            checkBoxCompleted.setChecked(task.isCompleted());

            // После обновления состояния снова добавляем слушатель
            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                listener.onTaskCompleted(task, isChecked);
            });

            buttonDelete.setOnClickListener(v -> listener.onTaskDeleted(task));
        }
    }

    public interface OnTaskClickListener {
        void onTaskCompleted(Task task, boolean isChecked);
        void onTaskDeleted(Task task);
    }
}
