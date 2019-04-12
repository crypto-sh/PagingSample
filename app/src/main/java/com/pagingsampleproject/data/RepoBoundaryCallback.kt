package com.pagingsampleproject.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.pagingsampleproject.api.GithubService
import com.pagingsampleproject.api.searchRepos
import com.pagingsampleproject.db.GithubLocalCache
import com.pagingsampleproject.model.RepoSearch
import com.spotifyplayer.enums.NetworkState

class RepoBoundaryCallback(
    private val query: String,
    private val service: GithubService,
    private val cache: GithubLocalCache
) : PagedList.BoundaryCallback<RepoSearch>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    // keep the last requested page.
    // When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    val networkErrors = MutableLiveData<NetworkState>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepoSearch) {
        requestAndSaveData(query)
    }


    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(NetworkState.error(error))
            isRequestInProgress = false

        })
    }
}