package com.example.capturetheflag.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.capturetheflag.fragments.PagerFragment
import com.example.capturetheflag.models.PageContent

class CircularViewPagerAdapter(fragmentManager: FragmentManager, private val pageContents: List<PageContent>) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return pageContents.size
        }

        override fun getItem(position: Int): Fragment {
            return PagerFragment.newInstance(pageContents[position])
        }
    }