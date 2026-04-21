package com.example.todolistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.Priority
import com.example.todolistapp.data.Task
import com.example.todolistapp.data.TaskDatabase
import com.example.todolistapp.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SortOrder {
    DATE, PRIORITY, TITLE
}

enum class FilterStatus {
    ALL, COMPLETED, PENDING
}

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    
    private val _sortOrder = MutableStateFlow(SortOrder.DATE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _filterStatus = MutableStateFlow(FilterStatus.ALL)
    val filterStatus: StateFlow<FilterStatus> = _filterStatus

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: StateFlow<List<Task>> = combine(_sortOrder, _filterStatus) { sort, filter ->
        Pair(sort, filter)
    }.flatMapLatest { (sort, filter) ->
        val taskFlow = when (sort) {
            SortOrder.DATE -> repository.getAllTasks()
            SortOrder.PRIORITY -> repository.getTasksSortedByPriority()
            SortOrder.TITLE -> repository.getTasksSortedByTitle()
        }
        
        taskFlow.map { list ->
            when (filter) {
                FilterStatus.ALL -> list
                FilterStatus.COMPLETED -> list.filter { it.isCompleted }
                FilterStatus.PENDING -> list.filter { !it.isCompleted }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onTaskCheckedChange(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = isCompleted))
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun onFilterStatusChange(filterStatus: FilterStatus) {
        _filterStatus.value = filterStatus
    }

    suspend fun getTaskById(id: Int): Task? {
        return repository.getTaskById(id)
    }

    fun upsertTask(
        id: Int = 0,
        title: String,
        description: String,
        dueDate: Long,
        priority: Priority,
        isCompleted: Boolean = false
    ) {
        viewModelScope.launch {
            val task = Task(
                id = id,
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                isCompleted = isCompleted
            )
            if (id == 0) {
                repository.insertTask(task)
            } else {
                repository.updateTask(task)
            }
        }
    }
}
