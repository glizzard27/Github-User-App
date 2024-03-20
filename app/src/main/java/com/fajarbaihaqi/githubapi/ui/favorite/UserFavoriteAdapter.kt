package com.fajarbaihaqi.githubapi.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.fajarbaihaqi.githubapi.data.db.UserFavorite
import com.fajarbaihaqi.githubapi.databinding.ItemRowUserBinding
import com.fajarbaihaqi.githubapi.ui.detail.DetailActivity

class UserFavoriteAdapter: ListAdapter<UserFavorite, UserFavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: UserFavorite){
            binding.tvItemName.text = itemName.username
            Glide.with(binding.root)
                .load(itemName.avatarUrl)
                .into(binding.imgItemPhoto)
            binding.root.setOnClickListener{
                val intentDetailUser = Intent(binding.root.context, DetailActivity::class.java)
                intentDetailUser.putExtra("ID", itemName.name)
                intentDetailUser.putExtra("USERNAME", itemName.username)
                intentDetailUser.putExtra("AVATAR", itemName.avatarUrl)
                binding.root.context.startActivity(intentDetailUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserFavorite>(){
            override fun areItemsTheSame(oldItem: UserFavorite, newItem: UserFavorite): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserFavorite, newItem: UserFavorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}