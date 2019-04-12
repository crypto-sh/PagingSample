package com.pagingsampleproject.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.pagingsampleproject.api.GithubService
import com.pagingsampleproject.api.searchRepos
import com.pagingsampleproject.model.RepoSearch
import com.spotifyplayer.enums.NetworkState

class RepoSearchDataSource(private val query: String,private val service: GithubService) : PageKeyedDataSource<Int, RepoSearch>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    private var lastRequestedPage = 1
    private var isRequestInProgress = false

    val networkState = MutableLiveData<NetworkState>()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, RepoSearch>) {
        if (isRequestInProgress) return
        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            //            cache.insert(repos) {
            lastRequestedPage++
            isRequestInProgress = false
            callback.onResult(repos,0,1)
//            }
        }, { error ->
            networkState.postValue(NetworkState.error(error))
            isRequestInProgress = false
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, RepoSearch>) {


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, RepoSearch>) {


    }

//    private fun requestData(query: String) {
//        if (isRequestInProgress) return
//
//        isRequestInProgress = true
//
//        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
//            //            cache.insert(repos) {
//            lastRequestedPage++
//            isRequestInProgress = false
//
//
//            //data.postValue(repos)
////            }
//        }, { error ->
//            //networkState.postValue(NetworkState.error(error))
//            isRequestInProgress = false
//        })
//    }

    class DataSourceFactory(
        private val query   : String,
        private val service : GithubService) : DataSource.Factory<Int, RepoSearch>(){

        val queryDataSource = MutableLiveData<RepoSearchDataSource>()

        override fun create(): DataSource<Int, RepoSearch> {
            val source = RepoSearchDataSource(query,service)
            queryDataSource.postValue(source)
            return source
        }
    }
}