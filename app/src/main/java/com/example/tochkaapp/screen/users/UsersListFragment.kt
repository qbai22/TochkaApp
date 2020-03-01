package com.example.tochkaapp.screen.users

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.databinding.FragmentUsersListBinding
import com.example.tochkaapp.databinding.ItemUserBinding
import com.example.tochkaapp.utils.State
import kotlinx.android.synthetic.main.fragment_users_list.*
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Created by Vladimir Kraev
 */
class UsersListFragment :
    Fragment(),
    UsersAdapter.UserItemNavigationListener,
    TextWatcher {

    private lateinit var viewModel: UsersListViewModel
    private lateinit var binding: FragmentUsersListBinding

    private lateinit var usersListAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(UsersListViewModel::class.java)
        usersListAdapter = UsersAdapter(viewModel, this)
        binding = FragmentUsersListBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
        }

        with(binding) {
            usersRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = usersListAdapter
                //required to play transition animations back to this destination
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
                addItemDecoration(UsersItemDecoration(context))
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.users.observe(this, Observer {
            usersListAdapter.submitList(it)
        }
        )
        viewModel.loadingState.observe(this, Observer {
            usersListAdapter.setNetworkState(it)
            if (it.state == State.LOADED && usersListAdapter.itemCount == 0)
                empty_list_text_view.visibility = View.VISIBLE
            else empty_list_text_view.visibility = View.GONE
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
    }

    private fun setupSearch() {
        search_edit_text.addTextChangedListener(this)
        search_edit_text.text = null
    }


    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.e(TAG, "on_query_change called ёбаный блять в рот и значение у него нахуй $s")
        viewModel.onQueryChanged(s.toString())
    }

    override fun navigate(user: User, binding: ItemUserBinding) {
        val action = UsersListFragmentDirections.actionRepositoriesFragmentToDetailsFragment(user)
        val extras = FragmentNavigatorExtras(
            binding.root.user_avatar_image_view to user.avatarUrl,
            binding.root.user_name_text_view to user.name
        )
        findNavController().navigate(action, extras)
    }

    companion object {
        private const val TAG = "USERS_LIST_FRAGMENT"
    }


}