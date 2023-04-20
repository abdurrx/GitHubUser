package com.dicoding.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.adapter.FragmentTypes
import com.dicoding.githubuser.adapter.UsersAdapter
import com.dicoding.githubuser.api.ResponseItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.viewmodel.FollowViewModel

class FollowFragment : Fragment() {
    private var _fragmentFollowBinding: FragmentFollowBinding? = null
    private val fragmentFollowBinding get() = _fragmentFollowBinding!!

    private val followViewModel: FollowViewModel by viewModels()
    private lateinit var listAdapter: UsersAdapter

    private var position: Int? = 0
    private var username: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentFollowBinding = FragmentFollowBinding.inflate(inflater, container, false)
        return fragmentFollowBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        val recyclerView: RecyclerView = fragmentFollowBinding.rvFollow
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Adapter
        listAdapter = UsersAdapter(arrayListOf<ResponseItem>(), FragmentTypes.DETAILS_FRAGMENT)
        recyclerView.adapter = listAdapter

        followViewModel.listFollow.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1){
            followViewModel.getFollower(username ?: null!!)
        } else {
            followViewModel.getFollowing(username ?: null!!)
        }

        // Loading
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentFollowBinding.progressBar.isVisible = it
        }

        // Toast
        followViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })
    }

    companion object{
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}