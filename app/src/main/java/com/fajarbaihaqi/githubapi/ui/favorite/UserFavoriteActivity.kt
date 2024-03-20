package com.fajarbaihaqi.githubapi.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajarbaihaqi.githubapi.databinding.ActivityUserFavoriteBinding
import com.fajarbaihaqi.githubapi.ui.ViewModelFactory

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFavoriteBinding
    private val userFavoriteViewModel by viewModels<UserFavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = "Pengguna Favorit"

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserFavorite.layoutManager = layoutManager

        userFavoriteViewModel.getAllFavoriteUsers().observe(this){favoriteUsers ->
            val adapter = UserFavoriteAdapter()
            adapter.submitList(favoriteUsers.sortedBy {it.name})
            binding.rvUserFavorite.adapter = adapter
        }
    }
}