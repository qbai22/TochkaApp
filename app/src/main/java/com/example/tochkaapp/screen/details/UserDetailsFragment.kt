package com.example.tochkaapp.screen.details

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
import com.example.tochkaapp.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.android.synthetic.main.item_user.*

/**
 * Created by Vladimir Kraev
 */

class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModelUser: UserDetailsViewModel

    val args: UserDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModelUser = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = this.viewModelUser
        binding.user = args.githubUser
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.githubUser
        user_details_picture.transitionName = user.avatarUrl
        binding.root.user_name_text_view.transitionName = user.name

        Glide.with(view.context)
            .load(user.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(user_details_picture)


    }
}