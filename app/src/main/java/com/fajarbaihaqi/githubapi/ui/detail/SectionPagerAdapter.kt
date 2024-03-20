package com.fajarbaihaqi.githubapi.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String):FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailUserFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailUserFragment.ARG_POSITION, position + 1)
            putString(DetailUserFragment.ARG_USERNAME, username)
        }
        return fragment
    }

}