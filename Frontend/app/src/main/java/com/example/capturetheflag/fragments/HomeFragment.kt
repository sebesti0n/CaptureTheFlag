package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.capturetheflag.ui.HomeFragmentViewModel
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.ViewPagerAdapter
import com.example.capturetheflag.databinding.FragmentHomefragmentBinding
import com.example.capturetheflag.models.PagerContent

class HomeFragment : Fragment() {
    private var _binding: FragmentHomefragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var mList:ArrayList<PagerContent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomefragmentBinding.inflate(inflater, container ,false)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
//        val viewPager: CircularViewPager = binding.viewPager
//
//        val pageContents = listOf(
//            PageContent(R.drawable.login_prev_ui, "Text for Page 1"),
//            PageContent(R.drawable.logo_register_removebg_preview, "Text for Page 2"),
//            PageContent(R.drawable.login, "Text for Page 3")
//        )
//
//        val adapter = CircularViewPagerAdapter(childFragmentManager, pageContents)
//        viewPager.adapter = adapter

        viewPager = binding.viewPager2
        loadcards()

    }

    private fun loadcards() {
        mList= ArrayList()
        mList.add(PagerContent(R.drawable.login_prev_ui,"text 1"))
        mList.add(PagerContent(R.drawable._d_render_grunge_style_interior_design,"text 2"))
        mList.add(PagerContent(R.drawable.login,"text 3"))
        viewPagerAdapter = context?.let { ViewPagerAdapter(it,mList) }!!
        viewPager.adapter=viewPagerAdapter
        viewPagerAdapter.setInitialPosition(viewPager)

    }
}