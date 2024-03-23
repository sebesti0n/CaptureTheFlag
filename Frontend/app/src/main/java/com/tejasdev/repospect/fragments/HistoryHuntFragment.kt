package com.tejasdev.repospect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tejasdev.repospect.adapters.EventAdapter
import com.tejasdev.repospect.databinding.FragmentMyEventsBinding
import com.tejasdev.repospect.models.Event
import com.tejasdev.repospect.ui.HistoryHuntViewModel
import com.tejasdev.repospect.util.EventItemClickListener

class HistoryHuntFragment:Fragment(),EventItemClickListener {
    private lateinit var viewModel: HistoryHuntViewModel
    private var _binding: FragmentMyEventsBinding?=null
    private val binding get() = _binding!!
    private lateinit var eList:ArrayList<Event>
    private lateinit var adapter:EventAdapter
    private lateinit var listner: EventItemClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listner = this
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
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
        viewModel.getHistoryEvents()
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