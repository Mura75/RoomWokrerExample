package com.prj.testroom.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
abstract class TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllTodos(list: List<TodoEntity>)

    @Query("SELECT * FROM todos")
    abstract fun getAllCards(): Single<List<TodoEntity>>
}