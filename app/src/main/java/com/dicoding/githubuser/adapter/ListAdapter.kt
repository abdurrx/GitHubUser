package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.api.ResponseItem
import com.dicoding.githubuser.databinding.ItemRowUserBinding
import com.dicoding.githubuser.fragment.DetailFragmentDirections
import com.dicoding.githubuser.fragment.ListFragmentDirections

enum class FragmentTypes {
    USERS_FRAGMENT,
    DETAILS_FRAGMENT
}

class UsersAdapter(private val listData: ArrayList<ResponseItem>, private val fragmentType: FragmentTypes) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    class ViewHolder(private val itemRowUserBinding: ItemRowUserBinding, private val fragment: FragmentTypes) : RecyclerView.ViewHolder(itemRowUserBinding.root) {
        fun bind(username: String, url: String) {
            with(itemRowUserBinding) {
                tvUsername.text = username

                Glide.with(itemView.context)
                    .load(url)
                    .into(ivPicture)

                cvUser.setOnClickListener {
                    if(fragment.equals(FragmentTypes.USERS_FRAGMENT)) {
                        navigateDetail(username, it)
                    }

                    else {
                        navigateDetailSelf(username, it)
                    }
                }
            }
        }

        private fun navigateDetail(username: String, view: View){
            val action = ListFragmentDirections.actionListFragmentToDetailFragment()
            action.username = username
            view.findNavController().navigate(action)
        }

        private fun navigateDetailSelf(username: String, view: View){
            val action = DetailFragmentDirections.actionDetailFragmentSelf()
            action.username = username
            view.findNavController().navigate(action)
        }
    }

    fun submitList(newList: List<ResponseItem>){
        val diffResult = DiffUtil.calculateDiff(ListDiffUtil(listData, newList))

        listData.clear()
        listData.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRowUserBinding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(itemRowUserBinding, fragmentType)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentData = listData[position]
        viewHolder.bind(currentData.login ?: "failed", currentData.avatarUrl ?: "failed")
    }

    override fun getItemCount(): Int = listData.size
}

class ListDiffUtil(
    private val oldList: List<ResponseItem>,
    private val newList: List<ResponseItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.login == newData.login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData == newData
    }
}