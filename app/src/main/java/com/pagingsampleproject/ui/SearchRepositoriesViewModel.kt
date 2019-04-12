package com.pagingsampleproject.ui

import android.arch.lifecycle.*
import android.arch.paging.PagedList
import android.content.Context
import com.pagingsampleproject.api.GithubService
import com.pagingsampleproject.data.GithubRepository
import com.pagingsampleproject.db.GithubLocalCache
import com.pagingsampleproject.db.RepoDataBase
import com.pagingsampleproject.model.RepoSearch
import com.pagingsampleproject.utils.AppExecuter
import com.spotifyplayer.enums.NetworkState


class SearchRepositoriesViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()

    private val repo = Transformations.map(queryLiveData) {
        repository.search(query = it)
    }

    val repos: LiveData<PagedList<RepoSearch>> = Transformations.switchMap(repo) {
        it.data
    }

    val networkState: LiveData<NetworkState> = Transformations.switchMap(repo) {
        it.networkStatus
    }


    fun searchRepositoeies(query: String) {
        queryLiveData.postValue(query)
    }

    fun lastQueryValue(): String? = queryLiveData.value

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = GithubService.create()

            val db = RepoDataBase.getInstance(context = context)

            val cache = GithubLocalCache(db.reposDao(), AppExecuter.getAppExecutor().diskIO)
            val repository = GithubRepository(service, cache)
            return SearchRepositoriesViewModel(repository) as T


        }
    }
}