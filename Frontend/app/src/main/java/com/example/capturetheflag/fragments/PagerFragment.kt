package com.example.capturetheflag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.capturetheflag.R
import com.example.capturetheflag.models.PageContent

class PagerFragment : Fragment() {
    companion object {
        private const val ARG_PAGE_CONTENT = "pageContent"

        fun newInstance(pageContent: PageContent): PagerFragment {
            val fragment = PagerFragment()
            val args = Bundle()
            args.putParcelable(ARG_PAGE_CONTENT, pageContent)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgview:ImageView = view.findViewById(R.id.img_viewpager)
        val pageContent = arguments?.getParcelable<PageContent>(ARG_PAGE_CONTENT)

        pageContent?.let {
            imgview.setImageResource(it.imageResId)
        }
    }
}