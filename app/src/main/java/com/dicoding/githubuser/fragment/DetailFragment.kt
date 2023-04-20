package com.dicoding.githubuser.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapter.DetailPagerAdapter
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.FragmentDetailBinding
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.dicoding.githubuser.viewmodel.DetailViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {
    private var _fragmentDetailBinding: FragmentDetailBinding? = null
    private val fragmentDetailBinding get() = _fragmentDetailBinding!!

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var username: String
    private lateinit var avatarUrl: String

    private lateinit var favFab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false)
        detailViewModel = ViewModelProvider(this, DetailViewModelFactory(requireActivity().application))[DetailViewModel::class.java]
        return fragmentDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Safe Args
        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username
        detailViewModel.getDetail(username)

        // Detail
        detailViewModel.detail.observe(viewLifecycleOwner) {
            setDetail(it.name, it.avatarUrl, it.followers, it.following)
        }

        // Loading
        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentDetailBinding.progressBar.isVisible = it
        }

        // Toast
        detailViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        // ViewPager2
        val sectionsPagerAdapter = DetailPagerAdapter(requireActivity(), username)
        val viewPager: ViewPager2 = fragmentDetailBinding.vpFollow
        viewPager.adapter = sectionsPagerAdapter

        // Tabs
        val tabs: TabLayout = fragmentDetailBinding.tabFollow

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TABS[position])
        }.attach()

        // Favorite FAB
        favFab = fragmentDetailBinding.fabFav
        detailViewModel.isFav(username)

        detailViewModel.isFavorite.observe(viewLifecycleOwner) {
            setFavoriteOnClickListener(it)
        }

    }

    private fun setDetail(name: String?, avatarUrl: String?, followers: Int?, following: Int) {
        if (avatarUrl != null) {
            this.avatarUrl = avatarUrl
        }

        with(fragmentDetailBinding){
            tvUsername.text = username
            tvName.text = name ?: ""

            tvFollowerTotal.text = followers.toString()
            tvFollowingTotal.text = following.toString()

            Glide.with(requireContext())
                .load(avatarUrl)
                .into(ivPicture)
        }
    }

    private fun setFavoriteOnClickListener(isFavorite: Boolean) {
        if (!isFavorite){
            favFab.setOnClickListener {
                favFab.setImageResource(R.drawable.ic_baseline_favorite_24)
                val favoriteUser = Favorite(username, avatarUrl)
                detailViewModel.insertFavorite(favoriteUser)
            }
        }

        else {
            favFab.setImageResource(R.drawable.ic_baseline_favorite_24)
            favFab.setOnClickListener {
                favFab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                detailViewModel.deleteFavorite(username)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentDetailBinding = null
    }

    companion object {
        @StringRes
        private val TABS = intArrayOf(
            R.string.follower_tab,
            R.string.following_tab
        )
    }
}