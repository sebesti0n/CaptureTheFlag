package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentRegisterHuntBinding
import com.example.capturetheflag.helper.NetworkHelper
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.RegisterHuntViewModel
import com.example.capturetheflag.util.EventItemClickListener
import com.google.android.material.snackbar.Snackbar
import okhttp3.internal.addHeaderLenient

class RegisterHuntFragment : Fragment(), EventItemClickListener{
    private lateinit var viewModel: RegisterHuntViewModel
    private var _binding: FragmentRegisterHuntBinding?=null
    private val binding get() = _binding!!
    private var eList: ArrayList<Event> = arrayListOf()
    private lateinit var adapter: EventAdapter
    private lateinit var listner: EventItemClickListener

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
        viewModel = ViewModelProvider(this)[RegisterHuntViewModel::class.java]
        setUpRecyclerView()

        if(NetworkHelper.isInternetAvailable(requireContext())){
            viewModel.getRegisteredEvents()
        }
        else{
            showSnackbar("Please connect to internet")
        }
        viewModel.getRegisteredEvents()

        viewModel.eventResponseLiveData.observe(viewLifecycleOwner, Observer {
            it?.let{ adapter.setdata(it.event) }
        })
    }

    private fun setUpRecyclerView(){
        adapter = EventAdapter(listner)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
    }

    override fun onEventClickListner(event: Event) {
        val action = RegisterHuntFragmentDirections.actionRegisterHuntFragmentToEventFragment(event.event_id.toLong())
        findNavController().navigate(action)
    }

    private fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, 2000).show()
    }

}