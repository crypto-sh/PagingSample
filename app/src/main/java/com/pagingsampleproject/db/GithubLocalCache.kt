package com.pagingsampleproject.db

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.pagingsampleproject.model.RepoSearch
import java.util.concurrent.Executor


class GithubLocalCache(private val repoDao: RepoDao, private val ioExecutor: Executor) {

    fun insert(repos : List<RepoSearch>, insertFinished : () -> Unit){
        ioExecutor.execute {
           repoDao.insert(repos)
            insertFinished()
        }
    }

    fun repoByName(name : String) : DataSource.Factory<Int,RepoSearch> {
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }

}