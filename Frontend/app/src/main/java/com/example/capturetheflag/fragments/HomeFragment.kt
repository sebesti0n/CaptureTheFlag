package com.example.capturetheflag.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.adapters.EventAdapter
import com.example.capturetheflag.databinding.FragmentHomefragmentBinding
import com.example.capturetheflag.helper.NetworkHelper
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.models.ResponseEventModel
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

        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        binding.searchCard.setOnClickListener {
            if(binding.searchBar.visibility == View.GONE){
                showSearchBar()
            }else{
                hideSearchBar()
            }
        }

        if(NetworkHelper.isInternetAvailable(requireContext())){
            viewModel.getLiveEvents()
        }
        else showSnackbar("Please connect to internet")

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

    private fun isTouchInsideView(view: View, event: MotionEvent): Boolean {
        val location = intArrayOf(0, 0)
        view.getLocationOnScreen(location)
        val x = event.rawX + view.left - location[0]
        val y = event.rawY + view.top - location[1]
        return (x >= 0 && x < view.width && y >= 0 && y < view.height)
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard(){
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun filterList(query: String){
        when(viewModel.liveEventResponseLiveData.value){
            is Resource.Success -> {
                updateAdapterData(query)
            }
            else ->{
                return
            }
        }
    }

    private fun showSearchBar(){
        binding.searchBar.visibility = View.VISIBLE
        showKeyboard()
        binding.searchEditText.requestFocus()
    }
    private fun hideSearchBar(){
        binding.searchBar.visibility = View.GONE
        hideKeyboard()
        binding.searchEditText.clearFocus()
    }

    private fun updateAdapterData(query: String){
        val list = viewModel.liveEventResponseLiveData.value!!.data?.event
        if(query.isNotEmpty()){
            list?.filter { it.title.contains(query) }?.let {
                ArrayList(
                    it
                )
            }?.let {
                adapter.setdata(
                    it
                )
            }
        }
        else list?.let { ArrayList(it) }?.let {
            adapter.setdata(
                it
            )
        }
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
