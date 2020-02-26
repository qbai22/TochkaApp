package com.example.tochkaapp.screen.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.databinding.ItemUserBinding

/**
 * Created by Vladimir Kraev
 */

class UserViewHolder private constructor(private val binding: ItemUserBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindUser(viewModel: UsersListViewModel, user: GithubUser) {
        binding.viewModel = viewModel
        binding.user = user
        Glide.with(binding.root).load(user.avatarUrl).circleCrop().into(binding.userAvatarImageView)
    }

    companion object {
        fun from(parent: ViewGroup): UserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserBinding.inflate(inflater, parent, false)
            return UserViewHolder(binding)
        }
    }
}