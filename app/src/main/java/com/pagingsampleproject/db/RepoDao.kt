package com.pagingsampleproject.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.pagingsampleproject.model.RepoSearch

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repos : List<RepoSearch>)

    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE :queryString) ORDER BY stars DESC, name ASC")
    fun reposByName(queryString: String): DataSource.Factory<Int,RepoSearch>

}