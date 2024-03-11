package com.sebesti0n.capturetheflag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sebesti0n.capturetheflag.R
import com.sebesti0n.capturetheflag.adapters.EventAdapter
import com.sebesti0n.capturetheflag.databinding.FragmentAdminHomeBinding
import com.sebesti0n.capturetheflag.models.Event
import com.sebesti0n.capturetheflag.ui.AdminHomeViewModel
import com.sebesti0n.capturetheflag.util.EventItemClickListener


class AdminHomeFragment : Fragment(),EventItemClickListener {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:EventAdapter
    private lateinit var viewModel: AdminHomeViewModel
    private lateinit var eList:ArrayList<Event>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AdminHomeViewModel::class.java]
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eList = ArrayList()
        adapter = EventAdapter(listner = this)
        binding.rvAdminEvent.adapter = adapter
        binding.rvAdminEvent.layoutManager = LinearLayoutManager(requireContext())
//        getEventList()
        adapter.setdata(eList)
        adapter.notifyDataSetChanged()
        Log.w("Sebastian nds",eList.toString())
        binding.fab.setOnClickListener{
        Navigation.findNavController(requireView()).navigate(R.id.action_FirstFragment_to_createEventFragment)
        }

    }

//    private fun getEventList() {
//        viewModel.getAdminEvents()
//        viewModel.get()?.observe(requireActivity()) {
//            Log.w("Sebastian it",it.event.toString())
//            eList = it.event
//            adapter.setdata(it.event)
//        }
//
//    }
    private fun getEventList() {
        viewModel.getAdminEvents()
        viewModel.eventResponseLiveData.observe(requireActivity()) {
            Log.w("Sebastian it", it.data?.event.toString())
            eList = it.data?.event!!
            adapter.setdata(eList)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClickListner(event: Event) {
       Log.w("sebastian dp",event.toString())
        val action = AdminHomeFragmentDirections.actionFirstFragmentToAdminEventFragment(event.event_id.toLong())
        findNavController().navigate(action)

    }
}