package com.fajarbaihaqi.githubapi.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fajarbaihaqi.githubapi.R
import com.fajarbaihaqi.githubapi.data.db.UserFavorite
import com.fajarbaihaqi.githubapi.databinding.ActivityDetailBinding
import com.fajarbaihaqi.githubapi.ui.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>{
        ViewModelFactory.getInstance(application)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME")
        val avatar = intent.getStringExtra("AVATAR")
        val url = intent.getStringExtra("URL")

        if(username != null){
            val sectionPagerAdapter = SectionPagerAdapter(this,username)
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
        if (username != null){
            detailUserViewModel.getDetailUser(username)
        }

        detailUserViewModel.userDetail.observe(this){
            if (it != null){
                Glide.with(this@DetailActivity)
                    .load(it.avatarUrl)
                    .centerCrop()
                    .into(binding.imgItemPhoto)
                binding.tvName.text = it.name
                binding.tvUserName.text = it.username
                binding.tvFollower.text = "${it.followersCount} follower"
                binding.tvFollowing.text = "${it.followingCount} following"
                binding.fabAddFavorite.contentDescription = it.isFavorite.toString()

                binding.apply {
                    if (!it.isFavorite){
                        fabAddFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity, R.drawable.baseline_favorite_border_24
                            )
                        )
                    } else {
                        fabAddFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@DetailActivity, R.drawable.baseline_favorite_24
                            )
                        )
                    }
                }
            }
        }
        detailUserViewModel.loading.observe(this){
            showLoading(it)
        }

        binding.apply {
            fabAddFavorite.setOnClickListener{
                val userFavorite = UserFavorite(
                    name = tvName.text.toString(),
                    username = tvUserName.text.toString(),
                    avatarUrl = avatar.toString(),
                    isFavorite = true,
                    followersCount = tvFollower.text.toString(),
                    followingCount = tvFollowing.text.toString()
                )

                val iconCurrent = fabAddFavorite.contentDescription
                if (iconCurrent == "true"){
                    fabAddFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity, R.drawable.baseline_favorite_border_24
                        )
                    )
                    detailUserViewModel.deleteFavorite(userFavorite)
                    fabAddFavorite.contentDescription = "false"
                } else {
                    fabAddFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DetailActivity, R.drawable.baseline_favorite_24
                        )
                    )
                    detailUserViewModel.insertFavorite(userFavorite)
                    fabAddFavorite.contentDescription = "true"
                }
            }
            fabShareBtn.setOnClickListener{
                val intent = Intent(Intent.ACTION_SEND)
                if (url != null){
                    intent.putExtra(Intent.EXTRA_TEXT, ("Follow On Github : $url"))
                } else {
                    intent.putExtra(Intent.EXTRA_TEXT, ("Follow On Github : $username"))
                }
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Send To"))
            }
        }
    }
    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}