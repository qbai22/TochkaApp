package com.example.tochkaapp.screen.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tochkaapp.R
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*

/**
 * Created by Vladimir Kraev
 */

class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    val args: UserDetailsFragmentArgs by navArgs()

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        user = args.user
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.user = user
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_details_picture.transitionName = user.avatarUrl
        binding.root.user_name_text_view.transitionName = user.name

        Glide.with(view.context)
            .load(user.avatarUrl)
            .placeholder(R.drawable.icon_avatar_placeholder)
            .apply(RequestOptions.circleCropTransform())
            .into(user_details_picture)

        profile_card.setOnClickListener { navigateToUserProfile() }
    }

    private fun navigateToUserProfile() {
        user.profileUrl.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view!!.context.startActivity(intent)
        }
    }

}