package com.example.tochkaapp.screen.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*

/**
 * Created by Vladimir Kraev
 */

class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: UserDetailsViewModel

    val args: UserDetailsFragmentArgs by navArgs()

    private lateinit var user: GithubUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        user = args.githubUser
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = this.viewModel
        binding.user = user
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_details_picture.transitionName = user.avatarUrl
        binding.root.user_name_text_view.transitionName = user.name

        Glide.with(view.context)
            .load(user.avatarUrl)
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