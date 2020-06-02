package com.prj.app_caching_paging.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prj.app_caching_paging.R
import com.prj.app_caching_paging.datasource.NetworkState
import com.prj.app_caching_paging.datasource.Status
import com.prj.app_caching_paging.repository.Movie

class MoviesAdapter(
    private val itemClickListener: ((item: Movie) -> Unit)? = null
) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(movieDiffCallback) {

    companion object {
        private val movieDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_movie -> {
                MovieViewHolder(
                    view = inflater.inflate(R.layout.item_movie, parent, false),
                    itemClickListener = itemClickListener
                )
            }
            R.layout.item_loading -> {
                ProgressViewHolder(
                    view = inflater.inflate(R.layout.item_loading, parent, false)
                )
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_movie -> {
                Log.d("view_type", "movie")
                getItem(position)?.let { (holder as MovieViewHolder).bind(it) }
            }
            R.layout.item_loading -> {
                Log.d("view_type", "progress")
                (holder as ProgressViewHolder).bind(networkState)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_loading
        } else {
            R.layout.item_movie
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class MovieViewHolder(
        private val view: View,
        private val itemClickListener: ((item: Movie) -> Unit)? = null
    ): RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView
        private val tvRating: TextView
        private val tvDescription: TextView
        private val tvDate: TextView
        private val ivPoster: ImageView

        init {
            tvTitle = view.findViewById(R.id.tvName)
            tvRating = view.findViewById(R.id.tvDate)
            ivPoster = view.findViewById(R.id.ivPoster)
            tvDescription = view.findViewById(R.id.tvDescription)
            tvDate = view.findViewById(R.id.tvGenre)
        }

        fun bind(item: Movie) {
            tvTitle.text = item.title
            item.voteAverage?.let { rating ->
                tvRating.text = "$rating/10"
            }
            item.releaseDate?.let { date ->
                tvDate.text = date
            }
            item.overview?.let { tvDescription.text = it }
            //val url = "${NetworkConstants.POSTER_BASE_URL}${item.posterPath}"
//            Log.d("path_url", url)
//            Glide.with(view.context)
//                .load(url)
//                .into(ivPoster)

            view.setOnClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }

    inner class ProgressViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        fun bind(networkState: NetworkState?) {
            when (networkState?.status) {
                Status.RUNNING -> progressBar.visibility = View.VISIBLE
                else -> progressBar.visibility = View.GONE
            }
        }
    }
}