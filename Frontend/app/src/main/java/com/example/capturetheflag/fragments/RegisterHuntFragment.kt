package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentRegisterHuntBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.HistoryHuntViewModel
import com.example.capturetheflag.ui.RegisterHuntViewModel
import com.example.capturetheflag.util.EventItemClickListner

class RegisterHuntFragment : Fragment(),EventItemClickListner{



    private lateinit var viewModel: RegisterHuntViewModel
    private var _binding: FragmentRegisterHuntBinding?=null
    private val binding get() = _binding!!
    private lateinit var eList:ArrayList<Event>
    private lateinit var adapter: EventAdapter
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
        viewModel = ViewModelProvider(this).get(RegisterHuntViewModel::class.java)
        eList = ArrayList()
        fetchPreviousEventListandSetupRecyclerview()
    }

    private fun fetchPreviousEventListandSetupRecyclerview() {
        adapter = EventAdapter(listner)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        viewModel.getRegisteredEvents(1)
        viewModel.get().observe(requireActivity()){
            eList = it.event
        }
        adapter.setdata(eList)
    }

    override fun onEventClickListner(event: Event) {
        val action = RegisterHuntFragmentDirections.actionRegisterHuntFragmentToEventFragment(event.event_id.toLong())
        findNavController().navigate(action)
    }

}