package com.dicoding.githubuser.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.R
import com.dicoding.githubuser.SettingPreferences
import com.dicoding.githubuser.adapter.FragmentTypes
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.api.ResponseItem
import com.dicoding.githubuser.databinding.FragmentListBinding
import com.dicoding.githubuser.viewmodel.ListViewModel
import com.dicoding.githubuser.viewmodel.ListViewModelFactory

class ListFragment : Fragment() {
    private var _fragmentListBinding: FragmentListBinding? = null
    private val fragmentListBinding get() = _fragmentListBinding!!

    private lateinit var listViewModel: ListViewModel
    private lateinit var listAdapter: UsersAdapter

    private lateinit var menu: Menu
    private var isDarkModeActive: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentListBinding = FragmentListBinding.inflate(inflater, container, false)

        val settingPreferences = SettingPreferences.getInstance(requireContext())
        listViewModel = ViewModelProvider(this, ListViewModelFactory(requireActivity().application, settingPreferences))[ListViewModel::class.java]

        return fragmentListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ToolBar Search
        val toolbar = fragmentListBinding.toolbar
        toolbar.inflateMenu(R.menu.search_menu)
        menu = toolbar.menu

        // Recycler View
        val recyclerView: RecyclerView = fragmentListBinding.rvList
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Adapter
        listAdapter = UsersAdapter(arrayListOf<ResponseItem>(), FragmentTypes.USERS_FRAGMENT)
        recyclerView.adapter = listAdapter

        listViewModel.listUser.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        // Loading
        listViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentListBinding.progressBar.isVisible = it
        }

        // Toast
        listViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // Search View
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(requireContext(), "User Not Found", Toast.LENGTH_SHORT).show()
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                listViewModel.getUsers(query)
                return true
            }
        })

        // Mode Display
        val mode = menu.findItem(R.id.setting)
        mode.setOnMenuItemClickListener {
            listViewModel.saveThemeSetting(!isDarkModeActive)
            true
        }

        listViewModel.getThemeSettings().observe(viewLifecycleOwner) {
            isDarkModeActive = it
            if(isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mode.setIcon(R.drawable.ic_baseline_dark_mode_24)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mode.setIcon(R.drawable.ic_baseline_light_mode_24)
            }
        }

        // Favorite Navigation
        val fav = menu.findItem(R.id.favorite)
        fav.setOnMenuItemClickListener {
            val action = ListFragmentDirections.actionListFragmentToFavoriteFragment()
            findNavController().navigate(action)
            true
        }
    }
}