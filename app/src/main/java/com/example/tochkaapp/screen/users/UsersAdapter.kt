package com.example.tochkaapp.screen.users

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tochkaapp.R
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.databinding.ItemUserBinding
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */
internal class UsersAdapter(
    private val viewModel: UsersListViewModel,
    private val navigationListener: UserItemNavigationListener
) : PagedListAdapter<GithubUser, RecyclerView.ViewHolder>(UserDiffCallback) {

    internal interface UserItemNavigationListener {
        fun navigate(user: GithubUser, binding: ItemUserBinding)
    }

    private var loadingState: LoadingState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder.create(parent, navigationListener)
            R.layout.item_loading_state -> LoadingStateViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user -> (holder as UserViewHolder).bindUser(
                viewModel,
                getItem(position)!!
            )
            R.layout.item_loading_state -> (holder as LoadingStateViewHolder).bindLoadingState(
                viewModel,
                loadingState!!
            )
        }
    }

    private fun hasExtraRow(): Boolean {
        return loadingState != null && loadingState != LoadingState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_loading_state
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