package com.pagingsampleproject.data

import android.arch.paging.LivePagedListBuilder
import com.pagingsampleproject.api.GithubService
import com.pagingsampleproject.db.GithubLocalCache
import com.pagingsampleproject.model.RepoSearchResult

class GithubRepository(
    private val service: GithubService,
    private val cache: GithubLocalCache
) {

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    fun search(query: String): RepoSearchResult {
        val dataSourceFactory = cache.repoByName(query)
        val boundaryCallback = RepoBoundaryCallback(query,service,cache)
        val networkState =  boundaryCallback.networkErrors

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RepoSearchResult(data, networkState)
    }

}