package com.example.todolistv2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    LiveData<List<Task>> getAllTasks();

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}
