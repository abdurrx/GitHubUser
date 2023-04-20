package com.dicoding.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.FragmentFavoriteBinding
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.dicoding.githubuser.viewmodel.FavoriteViewModelFactory

class FavoriteFragment : Fragment() {
    private var _fragmentFavoriteBinding: FragmentFavoriteBinding? = null
    private val fragmentFavoriteBinding get() = _fragmentFavoriteBinding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory(requireActivity().application))[FavoriteViewModel::class.java]
        return fragmentFavoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler View
        val recyclerView: RecyclerView = fragmentFavoriteBinding.rvFav
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Adapter
        favoriteAdapter = FavoriteAdapter(arrayListOf<Favorite>())
        recyclerView.adapter = favoriteAdapter

        favoriteViewModel.favorite.observe(viewLifecycleOwner) {
            favoriteAdapter.submitList(it)
        }

        // Loading
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentFavoriteBinding.progressBar.isVisible = it
        }

        favoriteViewModel.getAllFav()
    }
}