package com.example.todolistapp.repository

import com.example.todolistapp.data.Task
import com.example.todolistapp.data.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id)

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getTasksSortedByPriority(): Flow<List<Task>> = taskDao.getTasksSortedByPriority()

    fun getTasksSortedByTitle(): Flow<List<Task>> = taskDao.getTasksSortedByTitle()
}
