package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capturetheflag.ui.HomeFragmentViewModel
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.CircularViewPagerAdapter
import com.example.capturetheflag.databinding.FragmentHomefragmentBinding
import com.example.capturetheflag.models.PageContent
import com.example.capturetheflag.util.CircularViewPager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomefragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel

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
        val viewPager: CircularViewPager = binding.viewPager

        val pageContents = listOf(
            PageContent(R.drawable.login_prev_ui, "Text for Page 1"),
            PageContent(R.drawable.logo_register_removebg_preview, "Text for Page 2"),
            PageContent(R.drawable.login, "Text for Page 3")
        )

        val adapter = CircularViewPagerAdapter(childFragmentManager, pageContents)
        viewPager.adapter = adapter

    }
}