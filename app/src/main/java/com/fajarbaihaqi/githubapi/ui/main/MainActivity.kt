package com.fajarbaihaqi.githubapi.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajarbaihaqi.githubapi.R
import com.fajarbaihaqi.githubapi.data.response.GithubResponse
import com.fajarbaihaqi.githubapi.data.response.ItemsItem
import com.fajarbaihaqi.githubapi.data.retrofit.ApiConfig
import com.fajarbaihaqi.githubapi.databinding.ActivityMainBinding
import com.fajarbaihaqi.githubapi.ui.favorite.UserFavoriteActivity
import com.fajarbaihaqi.githubapi.ui.settings.SettingsActivity
import com.fajarbaihaqi.githubapi.ui.settings.SettingsPreferences
import com.fajarbaihaqi.githubapi.ui.settings.SettingsViewModel
import com.fajarbaihaqi.githubapi.ui.settings.SettingsViewModelFactory
import com.fajarbaihaqi.githubapi.ui.settings.dataStore
import retrofit2.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_NAME = "fajar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val preferences = SettingsPreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(preferences))[SettingsViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    mainViewModel.setReviewData(searchBar.text.toString())
                    mainViewModel.listUser.observe(this@MainActivity) {
                        if (it.isNullOrEmpty()) {
                            showNotFound(true)
                        } else {
                            showNotFound(false)
                        }
                    }
                    false
                }
            searchBar.setOnMenuItemClickListener { menuitem ->
                when (menuitem.itemId) {
                    R.id.menu_favorite -> {
                        val intent = Intent(this@MainActivity, UserFavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_settings -> {
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        mainViewModel.listUser.observe(this) {
            if (it != null) {
                setReviewData(it)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)
        findUser()
        }

    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUser(USER_NAME)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>,
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setReviewData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setReviewData(item: List<ItemsItem>) {
        val adapter = ListUserAdapter()
        adapter.submitList(item)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showNotFound(isDataNotFound: Boolean) {
        binding.apply {
            if (isDataNotFound) {
                rvUsers.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
            } else {
                rvUsers.visibility = View.VISIBLE
                errorMessage.visibility = View.GONE
            }
        }
    }
    }