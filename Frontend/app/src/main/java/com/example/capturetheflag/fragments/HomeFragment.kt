package com.example.capturetheflag.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment(),EventItemClickListener {
    private var _binding: FragmentHomefragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager
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
        viewPager = binding.viewPager2
//        loadcards()
        val isConnected = NetworkHelper.isInternetAvailable(requireContext())
        if(isConnected) {
            setupUpcomingEventRecyclerView()
            fetchUpcomingEventList()
            fetchLiveEventList()
        }
        else{
            Toast.makeText(requireContext(),"Network Unavailable",Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLiveEventList() {
        hideViewPager()
        showViewPagerProgressBar()
        viewModel.getLiveEvents()
        viewModel.getLive().observe(requireActivity()){
            if(it==null||it.event.size == 0){
                showSnackbar("No Live Events is Active")
                hideViewPager()
                hideViewPagerProgressBar()
            }
            else{
                mLiveList = it.event
                if(mLiveList.size > 0) {
                    loadcards()
                    showViewPager()
                }
                else
                    hideViewPager()
                hideViewPagerProgressBar()

            }

    }

    }
//
    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchUpcomingEventList() {
        hideRv()
        showRvProgressBar()
            viewModel.getUpcomingEvents()
            viewModel.get().observe(requireActivity()){
                if(it==null||it.event.size==0) {
                    showSnackbar("No Events Upcoming Events")
                    hideRvProgressBar()
                }
                else{
                    mUpcomingEvent = it.event
                    adapter.setdata(it.event)
                    adapter.notifyDataSetChanged()
                    hideRvProgressBar()
                    showRv()

                    Log.w("sebastian Home",it.event.toString())
                }
            }
            Log.w("sebastian Home",mUpcomingEvent.toString())
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
        CoroutineScope(Dispatchers.Main).launch{
            viewPagerAdapter = context?.let { ViewPagerAdapter(it, mLiveList, listner) }!!
            viewPager.adapter = viewPagerAdapter
            viewPagerAdapter.setInitialPosition(viewPager)
            hideViewPagerProgressBar()
        }
    }

    override fun onEventClickListner(event: Event) {
        val action = HomeFragmentDirections.actionHomefragmentToEventFragment(event.event_id.toLong())
        findNavController().navigate(action)
    }
    private fun showSnackbar(message:String){
        Toast.makeText(requireActivity(),message,Toast.LENGTH_LONG).show()
    }
    private fun showViewPagerProgressBar(){
        binding.vpProgressBar.visibility = View.VISIBLE
    }
    private fun hideViewPagerProgressBar(){
        binding.vpProgressBar.visibility = View.INVISIBLE
    }
    private fun showViewPager(){
        binding.viewPager2.visibility = View.VISIBLE
    }
    private fun hideViewPager(){
        binding.viewPager2.visibility = View.INVISIBLE
    }
    private fun showRvProgressBar(){
        binding.rvProgressBar.visibility = View.VISIBLE
    }
    private fun hideRvProgressBar(){
        binding.rvProgressBar.visibility = View.INVISIBLE
    }
    private fun showRv(){
        binding.recyclerView.visibility = View.VISIBLE
    }
    private fun hideRv(){
        binding.recyclerView.visibility = View.INVISIBLE
    }
}
