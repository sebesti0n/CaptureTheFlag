package com.example.capturetheflag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentHomefragmentBinding
import com.example.capturetheflag.helper.NetworkHelper
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.HomeFragmentViewModel
import com.example.capturetheflag.util.EventItemClickListener
import com.example.capturetheflag.util.LiveEventClickListner
import com.example.capturetheflag.util.Resource
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(),EventItemClickListener{
    private var _binding: FragmentHomefragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var mLiveList:ArrayList<Event>
    private lateinit var mUpcomingEvent: ArrayList<Event>
    private lateinit var adapter:EventAdapter
    private lateinit var listner: EventItemClickListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listner = this
        _binding = FragmentHomefragmentBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        initializeMembervariables()
        setUpRecyclerView()
        if(NetworkHelper.isInternetAvailable(requireContext())){
            viewModel.getLiveEvents()
        }
        else showSnackbar("Please connect to internet")
        binding.greetingText.text = "Hi, ${viewModel.enroll_id}"
        viewModel.liveEventResponseLiveData.observe(viewLifecycleOwner, Observer {

            when(it){
                is Resource.Error -> {
                    hideProgressBar()
                    showSnackbar("Some error occurred")
                }
                is Resource.Success -> {
                    hideProgressBar()
                    adapter.setdata(it.data!!.event)
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.eventsRcv.visibility = View.GONE
    }
    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
        binding.eventsRcv.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView(){
        adapter = EventAdapter(listner)
        binding.apply {
            eventsRcv.adapter = adapter
            eventsRcv.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchLiveEventList() {
        viewModel.getUpcomingEvents()
    }

    private fun initializeMembervariables() {
        mLiveList = ArrayList()
        mUpcomingEvent = ArrayList()
    }

    override fun onEventClickListner(event: Event) {
        moveToEventFragment(event)
    }

    private fun moveToEventFragment(event:Event){
        val isLive = isEventLive(event)
        val action = HomeFragmentDirections.actionHomefragmentToEventFragment(event.event_id.toLong(),isLive)
        findNavController().navigate(action)
    }

    private fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, 2000).show()
    }

    private fun isEventLive(event:Event):Boolean{
        val currentTimeMillis = System.currentTimeMillis()
        val startTimeMillis = event.start_ms.toLong()
        val endTimeMillis = event.end_ms.toLong()
        Log.d("CTF Home Fragment","currentMillisecond: ${currentTimeMillis}, start_ms: ${startTimeMillis}, end_ms: ${endTimeMillis}")
        return currentTimeMillis in startTimeMillis..endTimeMillis
    }
}
