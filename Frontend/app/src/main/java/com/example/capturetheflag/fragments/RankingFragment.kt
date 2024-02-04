package com.example.capturetheflag.fragments

import android.os.Bundle
import android.service.notification.NotificationListenerService.Ranking
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentRankingBinding
import com.example.capturetheflag.ui.ContestViewModel
import com.example.capturetheflag.ui.RankingViewModel

class RankingFragment : Fragment() {
    private var _binding:FragmentRankingBinding?=null
    private val binding get() = _binding!!
    private val args:RankingFragmentArgs by navArgs()
    private var eid=-1
    private lateinit var viewModel:RankingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eid=args.eid
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[RankingViewModel::class.java]
    }
}