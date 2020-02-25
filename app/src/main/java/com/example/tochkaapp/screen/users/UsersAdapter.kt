package com.example.tochkaapp.screen.users

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tochkaapp.R
import com.example.tochkaapp.utils.LoadingState
import com.example.tochkaapp.data.model.GithubUser

/**
 * Created by Vladimir Kraev
 */
class UsersAdapter(private val viewModel: UsersListViewModel)
    : PagedListAdapter<GithubUser, RecyclerView.ViewHolder>(UserDiffCallback) {

    private var loadingState: LoadingState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder.from(parent)
        //    R.layout.item_network_state -> NetworkStateViewHolder.from(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as UserViewHolder).bindUser(viewModel, getItem(position)!!)
        //    R.layout.item_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return loadingState != null && loadingState != LoadingState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_user
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    /**
     * Set the current network state to the adapter
     * but this work only after the initial load
     * and the adapter already have list to add new loading raw to it
     * so the initial loading state the activity responsible for handle it
     *
     * @param newLoadingState the new network state
     */
    fun setNetworkState(newLoadingState: LoadingState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.loadingState
                val hadExtraRow = hasExtraRow()
                this.loadingState = newLoadingState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newLoadingState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        val UserDiffCallback = object : DiffUtil.ItemCallback<GithubUser>() {
            override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                return oldItem == newItem
            }
        }
    }

}