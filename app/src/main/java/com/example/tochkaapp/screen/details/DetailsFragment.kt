package com.example.tochkaapp.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.tochkaapp.databinding.FragmentDetailsBinding

/**
 * Created by Vladimir Kraev
 */

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = this.viewModel
        return binding.root
    }


}