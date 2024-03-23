package com.tejasdev.repospect.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tejasdev.repospect.adapters.EventAdapter
import com.tejasdev.repospect.helper.NetworkHelper
import com.tejasdev.repospect.models.Event
import com.tejasdev.repospect.ui.RegisterHuntViewModel
import com.tejasdev.repospect.util.EventItemClickListener
import com.tejasdev.repospect.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.tejasdev.repospect.databinding.FragmentMyEventsBinding


class MyEventsFragment : Fragment(), EventItemClickListener{
    private lateinit var viewModel: RegisterHuntViewModel
    private var _binding:FragmentMyEventsBinding?=null
    private val binding get() = _binding!!
    private var eList: ArrayList<Event> = arrayListOf()
    private lateinit var adapter: EventAdapter
    private lateinit var listner: EventItemClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listner = this
        _binding = FragmentMyEventsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterHuntViewModel::class.java]
        setUpRecyclerView()

        if(NetworkHelper.isInternetAvailable(requireContext())){
            viewModel.getAllEvents()
        }
        else{
            showSnackbar("Network unavailable")
        }

        viewModel.eventResponseLiveData.observe(viewLifecycleOwner, Observer {

            when(it){
                is Resource.Success -> {
                    hideProgessBar()
                    it.data?.let{responseEventModel ->
                        val eventList = responseEventModel.event
                        val sortedList = eventList.sortedByDescending { it.end_ms }
                        adapter.setdata(
                            ArrayList(sortedList)
                        )
                    }
                }
                is Resource.Loading -> showProgressBar()
                is Resource.Error -> {
                    hideProgessBar()
                    showSnackbar("Error fetching details")
                }
            }
        })
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
    }
    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyboard(){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun filterList(query: String){
        when(viewModel.eventResponseLiveData.value){
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
        val list = viewModel.eventResponseLiveData.value!!.data?.event
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
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideProgessBar(){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }


    private fun setUpRecyclerView(){
        adapter = EventAdapter(listner)
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
    }

    override fun onEventClickListner(event: Event) {
        val action = MyEventsFragmentDirections.actionRegisterHuntFragmentToEventFragment(
            event.event_id.toLong(),isEventLive(event)
        )
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