package com.example.tochkaapp.screen.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tochkaapp.R
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.databinding.ItemUserBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Created by Vladimir Kraev
 */

internal class UserViewHolder private constructor(
    private val binding: ItemUserBinding,
    private val navigationListener: UsersAdapter.UserItemNavigationListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindUser(userListViewModel: UsersListViewModel, user: User) {
        with(binding) {
            viewModel = userListViewModel
            this.user = user
        }
        binding.root.user_avatar_image_view.transitionName = user.avatarUrl
        binding.root.user_name_text_view.transitionName = user.name
        binding.root.setOnClickListener { navigationListener.navigate(user, binding) }
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .placeholder(R.drawable.icon_avatar_placeholder)
            .circleCrop()
            .into(binding.userAvatarImageView)
    }

    companion object {
        fun create(parent: ViewGroup, navigationListener: UsersAdapter.UserItemNavigationListener): UserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserBinding.inflate(inflater, parent, false)
            return UserViewHolder(binding, navigationListener)
        }
    }
}