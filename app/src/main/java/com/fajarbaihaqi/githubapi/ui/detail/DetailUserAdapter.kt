package com.fajarbaihaqi.githubapi.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.fajarbaihaqi.githubapi.data.response.ItemsItem
import com.fajarbaihaqi.githubapi.databinding.ItemRowUserBinding

class DetailUserAdapter: ListAdapter<ItemsItem, DetailUserAdapter.MyViewHolder>(DIFF_CALLBACK){
    class MyViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: ItemsItem){
            binding.tvItemName.text = itemName.login
            Glide.with(binding.root).load(itemName.avatarUrl).into(binding.imgItemPhoto)
            binding.root.setOnClickListener{
                val intentDetailUser = Intent(binding.root.context, DetailActivity::class.java)
                intentDetailUser.putExtra("ID", itemName.id)
                intentDetailUser.putExtra("USERNAME", itemName.login)
                intentDetailUser.putExtra("AVATAR", itemName.avatarUrl)
                binding.root.context.startActivity(intentDetailUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }

    }


}