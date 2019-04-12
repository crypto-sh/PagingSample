package com.pagingsampleproject.ui


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.pagingsampleproject.R
import com.pagingsampleproject.model.RepoSearch
import com.spotifyplayer.enums.Status
import kotlinx.android.synthetic.main.activity_search_repositories.*


class SearchRepositoriesActivity : AppCompatActivity() {

    private val adapter = ReposAdapter()

    lateinit var viewModel: SearchRepositoriesViewModel

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)

        viewModel = ViewModelProviders.of(this,SearchRepositoriesViewModel.Factory(this@SearchRepositoriesActivity)).get(SearchRepositoriesViewModel::class.java)
        initAdapter()

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchRepositoeies(query)

        initSearch(query)

        hideKeyboard(list)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    fun initAdapter(){
        list.adapter = adapter
        viewModel.repos.observe(this, Observer<PagedList<RepoSearch>> {
            Log.d("Activity", "list: ${it?.size}")
//            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            when(it!!.status){
                Status.FAILED -> {
                    Toast.makeText(this, "\uD83D\uDE28 Wooops ${it.msg}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "\uD83D\uDE28 Wooops ${it.status}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initSearch(query : String) {
        search_repo.setText(query)
        search_repo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        search_repo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                viewModel.searchRepositoeies(it.toString())
                adapter.submitList(null)
            }
        }
    }

}
