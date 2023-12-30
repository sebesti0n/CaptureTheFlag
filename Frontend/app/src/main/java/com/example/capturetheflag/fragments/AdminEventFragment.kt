package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.capturetheflag.databinding.FragmentEventBinding
import com.example.capturetheflag.ui.EventViewModel


class AdminEventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val args: AdminEventFragmentArgs by navArgs()
    private lateinit var viewModel:EventViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.w("sebastian idk","sex")
        val eid = args.eid
        if (eid.toInt() ==-1){
            binding.contentDescription.text =  "Not found"
            binding.contentDetails.text = "Not Found"
            binding.contentPrizes.text = "Not Found"
        }
        else{
        viewModel.getAdminEventbyId(eid.toInt())
        viewModel.get()?.observe(requireActivity()) {
            val event = it.event.get(0)
            binding.contentDescription.text =  event.description
            binding.contentDetails.text = "Start At: ${event.start_time} \n End At: ${event.end_time}"
            binding.contentPrizes.text = "Amazing Goodies"
        }
        }

    }

}