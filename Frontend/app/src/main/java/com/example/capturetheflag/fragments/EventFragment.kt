package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capturetheflag.databinding.FragmentEventBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.EventViewModel

class EventFragment : Fragment() {
    private var _binding:FragmentEventBinding?=null
    private val binding get() = _binding!!
    private val args: EventFragmentArgs by navArgs()
    private lateinit var viewModel: EventViewModel
    private var eid:Long=-1
    private lateinit var event: Event
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.w("sebastian idk","sex")
        eid = args.eid
        updateUI()
        fetchRegisterStatusforEventOnOpen()
        binding.btnRegisteredEvent.setOnClickListener {
//        fetchRegisterStatusforEvent()
            val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
            findNavController().navigate(action)
        }

    }

    private fun fetchRegisterStatusforEventOnOpen() {
        viewModel.getFirstStatus(1,eid.toInt())
        viewModel.onOpenStatus().observe(requireActivity()){
            if(it==1)
                binding.btnRegisteredEvent.setText("Unregister")
            else
                binding.btnRegisteredEvent.setText("Register")
        }
    }

    private fun fetchRegisterStatusforEvent() {
        viewModel.registerUserForEvent(1,eid.toInt())
        viewModel.getStatus().observe(requireActivity()) {
            if(it==1)
                binding.btnRegisteredEvent.setText("Unregister")
            else
                binding.btnRegisteredEvent.setText("Register")
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        if (eid.toInt() ==-1){
            binding.contentDescription.text =  "Not found"
            binding.contentDetails.text = "Not Found"
            binding.contentPrizes.text = "Not Found"
        }
        else{
            viewModel.getAdminEventbyId(eid.toInt())
            viewModel.get()?.observe(requireActivity()) {
                event = it.event.get(0)
                Log.w("sebastian","event")
                binding.contentDescription.text =  event.description
                binding.contentDetails.text = "Start At: ${event.start_time} \n End At: ${event.end_time}"
                binding.contentPrizes.text = "Amazing Goodies"
                val imgview = binding.banner
                Glide.with(requireContext())
                    .load(event.posterImage)
                    .into(imgview)

            }

        }
    }
}