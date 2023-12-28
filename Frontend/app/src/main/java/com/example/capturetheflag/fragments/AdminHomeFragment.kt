package com.example.capturetheflag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentAdminHomeBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.util.EventItemClickListner


class AdminHomeFragment : Fragment(),EventItemClickListner {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:EventAdapter
    private lateinit var eList:ArrayList<EventX>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eList = ArrayList()
        adapter = EventAdapter(requireContext(), listner = this)
        binding.rvAdminEvent.adapter = adapter
        binding.rvAdminEvent.layoutManager = LinearLayoutManager(requireContext())
        adapter.setdata(eList)
        binding.fab.setOnClickListener{
        Navigation.findNavController(requireView()).navigate(R.id.action_FirstFragment_to_createEventFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClickListner() {
        TODO("Not yet implemented")
    }
}