package com.example.tochkaapp.screen.users

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tochkaapp.R
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.databinding.FragmentUsersListBinding
import com.example.tochkaapp.databinding.ItemUserBinding
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Created by Vladimir Kraev
 */
class UsersListFragment : Fragment(), SearchView.OnQueryTextListener,
    UsersAdapter.UserItemNavigationListener {

    private lateinit var viewModel: UsersListViewModel
    private lateinit var binding: FragmentUsersListBinding

    private lateinit var usersListAdapter: UsersAdapter

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
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.allUsers.observe(this, Observer { usersListAdapter.submitList(it) })
        viewModel.searchedUsers.observe(this, Observer { usersListAdapter.submitList(it) })
        viewModel.loadingState.observe(this, Observer {
            usersListAdapter.setNetworkState(it)
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_list, menu)
        val searchItem = menu.findItem(R.id.search_item)
        val searchView: SearchView = searchItem.actionView as SearchView
        setupSearchView(searchView, searchItem)
    }

    private fun setupSearchView(searchView: SearchView, searchMenuItem: MenuItem) {

        searchView.apply {
            setOnQueryTextListener(this@UsersListFragment)
            queryHint = getString(R.string.search_hint)
            maxWidth = Integer.MAX_VALUE
        }
        /*   val previousFilter = viewModel.getFilterInput()
           previousFilter?.let {
               if (it.isNotEmpty()) {
                   searchMenuItem.expandActionView()
                   searchView.setQuery(previousFilter, false)
               }
           }*/
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (query.trim().isNotEmpty())
            viewModel.searchUsers(query)
        return true
    }


    override fun onQueryTextChange(query: String): Boolean {
        return true
    }

    override fun navigate(user: GithubUser, binding: ItemUserBinding) {
        val action = UsersListFragmentDirections.actionRepositoriesFragmentToDetailsFragment(user)
        val extras = FragmentNavigatorExtras(
            binding.root.user_avatar_image_view to user.avatarUrl,
            binding.root.user_name_text_view to user.name
        )
        findNavController().navigate(action, extras)
    }


    companion object {
        private const val TAG = "WELCOME_FRAGMENT"
    }


}