package com.prj.app_caching_paging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prj.app_caching_paging.ui.MoviesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)
    }

    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.networkStateLiveData.observe(this, Observer { result ->
            adapter.setNetworkState(result)
            swipeRefreshLayout.isRefreshing = false
        })
        viewModel.dataSourceLiveData.observe(this, Observer { result ->
            swipeRefreshLayout.isRefreshing = false
            adapter.submitList(result)
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.retry()
        }

    }

}
