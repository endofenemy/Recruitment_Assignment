package com.githubrepo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepo.data.db.entities.Repository

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllRepository(repositories: List<Repository>)

    @Query("SELECT * FROM REPOSITORY")
    fun getAllRepository(): LiveData<List<Repository>>

}