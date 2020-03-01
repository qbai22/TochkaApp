package com.example.tochkaapp.screen.users

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tochkaapp.R
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
    SearchView.OnQueryTextListener,
    UsersAdapter.UserItemNavigationListener {

    private lateinit var viewModel: UsersListViewModel
    private lateinit var binding: FragmentUsersListBinding

    private lateinit var usersListAdapter: UsersAdapter

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_list, menu)
        val searchItem = menu.findItem(R.id.search_item)
        searchView = searchItem.actionView as SearchView

        setupSearchView(searchView, searchItem)
    }

    //SearchView это мýка :)
    private fun setupSearchView(searchView: SearchView, searchMenuItem: MenuItem) {
        searchView.apply {
            setOnQueryTextListener(this@UsersListFragment)
            queryHint = getString(R.string.search_hint)
            maxWidth = Integer.MAX_VALUE
        }
        val lastQuery = viewModel.getLastQuery()
        lastQuery?.let {
            if (it.isNotEmpty()) {
                searchMenuItem.expandActionView()
                searchView.setQuery(lastQuery, false)
                searchView.clearFocus()
            }
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(query: String): Boolean {
        Log.e(TAG, "on_query_change called ёбаный блять в рот и значение у него нахуй $query")
        viewModel.onQueryChanged(query)
        return true
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