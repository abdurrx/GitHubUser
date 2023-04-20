package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.ItemRowUserBinding
import com.dicoding.githubuser.fragment.FavoriteFragmentDirections

class FavoriteAdapter(private val listData: ArrayList<Favorite>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    class ViewHolder(private val itemRowUserBinding: ItemRowUserBinding) : RecyclerView.ViewHolder(itemRowUserBinding.root) {
        fun bind(username: String, url: String) {
            with(itemRowUserBinding) {
                tvUsername.text = username

                Glide.with(itemView.context)
                    .load(url)
                    .into(ivPicture)

                cvUser.setOnClickListener {
                    navigateDetail(username, it)
                }
            }
        }

        private fun navigateDetail(username: String, view: View){
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
            action.username = username
            view.findNavController().navigate(action)
        }
    }

    fun submitList(newList: List<Favorite>){
        val diffResult = DiffUtil.calculateDiff(FavDiffUtil(listData, newList))

        listData.clear()
        listData.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemRowUserBinding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(itemRowUserBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentData = listData[position]
        viewHolder.bind(currentData.username, currentData.avatarUrl)
    }

    override fun getItemCount(): Int = listData.size
}

class FavDiffUtil(
    private val oldList: List<Favorite>,
    private val newList: List<Favorite>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.username == newData.username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData == newData
    }
}