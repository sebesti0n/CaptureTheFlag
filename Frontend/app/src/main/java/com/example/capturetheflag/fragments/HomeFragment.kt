package com.example.capturetheflag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.capturetheflag.R
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.adapters.ViewPagerAdapter
import com.example.capturetheflag.databinding.FragmentHomefragmentBinding
import com.example.capturetheflag.helper.NetworkHelper
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.PagerContent
import com.example.capturetheflag.ui.HomeFragmentViewModel
import com.example.capturetheflag.util.EventItemClickListener


class HomeFragment : Fragment(),EventItemClickListener {
    private var _binding: FragmentHomefragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var mList:ArrayList<PagerContent>
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

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMembervariables()
        viewPager = binding.viewPager2
        loadcards()
        val isConnected = NetworkHelper.isInternetAvailable(requireContext())
        if(isConnected) {
//            setupUpcomingEventRecyclerView()
//            fetchUpcomingEventList()
//            fetchLiveEventList()
        }
        else{
            Toast.makeText(requireContext(),"Network Unavailable",Toast.LENGTH_SHORT).show()
        }
        binding.headingUpcomingEvent.setOnClickListener {
            val action = HomeFragmentDirections.actionHomefragmentToContestFragment()
            findNavController().navigate(action)
        }
    }

    private fun fetchLiveEventList() {
        viewModel.getLiveEvents()
        viewModel.getLive().observe(requireActivity()){
            mLiveList = it.event
        }
    }

    private fun fetchUpcomingEventList() {
    viewModel.getUpcomingEvents()
        viewModel.get().observe(requireActivity()){
            mUpcomingEvent = it.event
        }
        adapter.setdata(mUpcomingEvent)
    }

    private fun setupUpcomingEventRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initializeMembervariables() {
        mLiveList = ArrayList()
        mUpcomingEvent = ArrayList()
        adapter = EventAdapter(listner)
    }

    private fun loadcards() {
        mList= ArrayList()
        mList.add(PagerContent(R.drawable.login_prev_ui,"text 1"))
        mList.add(PagerContent(R.drawable.bg_login,"text 2"))
        mList.add(PagerContent(R.drawable.login,"text 3"))
        viewPagerAdapter = context?.let { ViewPagerAdapter(it,mList) }!!
        viewPager.adapter=viewPagerAdapter
        viewPagerAdapter.setInitialPosition(viewPager)

    }

    override fun onEventClickListner(event: Event) {
        val action = HomeFragmentDirections.actionHomefragmentToEventFragment(event.event_id.toLong())
        findNavController().navigate(action)
    }


}
