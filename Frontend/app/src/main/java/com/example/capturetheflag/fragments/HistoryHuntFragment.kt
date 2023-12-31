package com.example.capturetheflag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentRegisterBinding
import com.example.capturetheflag.databinding.FragmentRegisterHuntBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.ResponseEventModel
import com.example.capturetheflag.ui.HistoryHuntViewModel
import com.example.capturetheflag.util.EventItemClickListner

class HistoryHuntFragment:Fragment(),EventItemClickListner {
    private lateinit var viewModel: HistoryHuntViewModel
    private var _binding: FragmentRegisterHuntBinding?=null
    private val binding get() = _binding!!
    private lateinit var eList:ArrayList<Event>
    private lateinit var adapter:EventAdapter
    private lateinit var listner: EventItemClickListner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listner = this
        _binding = FragmentRegisterHuntBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryHuntViewModel::class.java)
        eList = ArrayList()
        fetchPreviousEventListandSetupRecyclerview()
    }

    private fun fetchPreviousEventListandSetupRecyclerview() {
        adapter = EventAdapter(listner)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        viewModel.getHistoryEvents(1)
        viewModel.get().observe(requireActivity()){
            eList = it.event
        }
        adapter.setdata(eList)
    }

    override fun onEventClickListner(event: Event) {
            val action = HistoryHuntFragmentDirections.actionHistoryHuntFragmentToEventFragment(event.event_id.toLong())
            findNavController().navigate(action)
    }
}