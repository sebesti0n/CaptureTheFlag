package com.example.capturetheflag.util

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class CircularViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
        override fun setCurrentItem(item: Int) {
            super.setCurrentItem(getCircularPosition(item), false)
        }

        override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
            super.setCurrentItem(getCircularPosition(item), smoothScroll)
        }

        private fun getCircularPosition(position: Int): Int {
            val pageCount = adapter?.count ?: 0
            if (pageCount == 0) return 0

            return if (position < 0) {
                pageCount + (position % pageCount)
            } else {
                position % pageCount
            }
        }

        override fun setAdapter(adapter: PagerAdapter?) {
            super.setAdapter(adapter)
            setCurrentItem(0, false)
        }
    }
