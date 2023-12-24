package com.example.capturetheflag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.capturetheflag.R
import com.example.capturetheflag.models.PagerContent

class ViewPagerAdapter(private val context:Context, private val list:ArrayList<PagerContent>): PagerAdapter() {

    override fun getCount(): Int {
return  Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (list.isEmpty()) {
            return super.instantiateItem(container, position)
        }

        val adjustedPosition = if (list.size > 0) {
            position % list.size
        } else {
            0
        }

        val view = LayoutInflater.from(context).inflate(R.layout.item_pager, container, false)
        val ivPager = view.findViewById<ImageView>(R.id.iv_pager)

        if (list.size > 0) {
            ivPager.setImageResource(list[adjustedPosition].imageResId)
        }

        view.setOnClickListener {
            Toast.makeText(context, "view Pager clicked", Toast.LENGTH_SHORT).show()
        }

        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    fun setInitialPosition(viewPager: ViewPager) {
        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % list.size
        viewPager.setCurrentItem(initialPosition, false)
    }
}