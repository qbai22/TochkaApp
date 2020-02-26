package com.example.tochkaapp.screen.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tochkaapp.databinding.ItemLoadingStateBinding
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */

internal class LoadingStateViewHolder private constructor(private val binding: ItemLoadingStateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindLoadingState(viewModel: UsersListViewModel, loadingState: LoadingState) {
        binding.viewModel = viewModel
        binding.loadingState = loadingState
    }

    companion object {
        fun create(parent: ViewGroup): LoadingStateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemLoadingStateBinding.inflate(inflater, parent, false)
            return LoadingStateViewHolder(binding)
        }
    }

}