package com.example.tochkaapp.screen.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.databinding.ItemUserBinding
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Created by Vladimir Kraev
 */

internal class UserViewHolder private constructor(
    private val binding: ItemUserBinding,
    private val navigationListener: UsersAdapter.UserItemNavigationListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindUser(userListViewModel: UsersListViewModel, githubUser: GithubUser) {
        with(binding) {
            viewModel = userListViewModel
            user = githubUser
        }
        binding.root.user_avatar_image_view.transitionName = githubUser.avatarUrl
        binding.root.user_name_text_view.transitionName = githubUser.name
        binding.root.setOnClickListener { navigationListener.navigate(githubUser, binding) }
        Glide.with(binding.root).load(githubUser.avatarUrl).circleCrop().into(binding.userAvatarImageView)
    }

    companion object {
        fun create(parent: ViewGroup, navigationListener: UsersAdapter.UserItemNavigationListener): UserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserBinding.inflate(inflater, parent, false)
            return UserViewHolder(binding, navigationListener)
        }
    }
}