package com.prj.testroom

import android.util.Log
import com.prj.testroom.database.TodoDao
import com.prj.testroom.database.TodoEntity
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) {

    fun saveAll(list: List<TodoEntity>) {
        todoDao.insertAllTodos(list)
    }
}