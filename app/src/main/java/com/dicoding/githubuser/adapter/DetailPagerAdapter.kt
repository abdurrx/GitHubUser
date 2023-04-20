package com.dicoding.githubuser.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.fragment.FollowFragment

class DetailPagerAdapter(activity: FragmentActivity, username: String) : FragmentStateAdapter(activity) {
    private var username: String
    override fun getItemCount(): Int = 2

    init { this.username = username }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }

        return fragment
    }
}