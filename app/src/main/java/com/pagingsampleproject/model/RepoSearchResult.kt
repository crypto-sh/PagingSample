package com.pagingsampleproject.model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.spotifyplayer.enums.NetworkState

data class RepoSearchResult(
    val data : LiveData<PagedList<RepoSearch>>,
    val networkStatus : LiveData<NetworkState>)