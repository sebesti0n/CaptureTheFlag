package com.example.capturetheflag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.capturetheflag.R
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.PagerContent
import com.example.capturetheflag.util.EventItemClickListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewPagerAdapter(private val context:Context, private val list:ArrayList<Event>,private val listner: EventItemClickListener): PagerAdapter() {

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
                var imageurl =
                    "https://firebasestorage.googleapis.com/v0/b/capture-the-flag-9f489.appspot.com/o/images%2F1000094308?alt=media&token=71f9154e-2765-4391-8019-9fb209abd0a5"
                if (list[adjustedPosition].posterImage != "") imageurl = list[adjustedPosition].posterImage
                Glide.with(context)
                    .load(imageurl)
                    .placeholder(R.drawable.pleasewait)
                    .into(ivPager)
            }

        view.setOnClickListener {
            listner.onEventClickListner(list[adjustedPosition])
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