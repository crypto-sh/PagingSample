package com.pagingsampleproject.ui

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.pagingsampleproject.model.RepoSearch

class ReposAdapter : PagedListAdapter<RepoSearch,RepoViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoSearch>() {
            override fun areItemsTheSame(oldItem: RepoSearch, newItem: RepoSearch): Boolean = oldItem.fullName == newItem.fullName
            override fun areContentsTheSame(oldItem: RepoSearch, newItem: RepoSearch): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RepoViewHolder {
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder : RepoViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }
}