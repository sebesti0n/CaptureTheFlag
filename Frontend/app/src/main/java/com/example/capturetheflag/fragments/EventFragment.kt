package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class EventFragment : Fragment() {
    private var _binding:FragmentEventBinding?=null
    private val binding get() = _binding!!
    private val args: EventFragmentArgs by navArgs()
    private lateinit var viewModel: EventViewModel
    private var eid:Long=-1
    private lateinit var event: Event
    private var isLive = false
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
        fetchRegisterStatusforEvent()
            val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
            findNavController().navigate(action)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun fetchRegisterStatusforEventOnOpen() {
        viewModel.getFirstStatus(eid.toInt())
        viewModel.onOpenStatus().observe(requireActivity()){
            if (it==3){
                isLive = true
            }
            if(it==1)
                binding.btnRegisteredEvent.text = "Unregister"
            else
                binding.btnRegisteredEvent.text = "Register"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchRegisterStatusforEvent() {
        viewModel.registerUserForEvent(eid.toInt())
        viewModel.getStatus().observe(requireActivity()) {
            if(it==1){
                binding.btnRegisteredEvent.text = "Start"
            }
            else if(it==2)
                binding.btnRegisteredEvent.text = "Unregister"
            else
                binding.btnRegisteredEvent.text = "Register"
        }

    }
    private fun setCountDownTimer(startTime:String){
        val currTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val startDate = sdf.parse(startTime)?.time ?: 0
        val timeToStart = startDate-currTime
        val countDownTimer = object:CountDownTimer(timeToStart,1000){
            override fun onTick(millisUntilFinished: Long) {
                val timeString = getFormattedTime(millisUntilFinished)
                binding.countDownTimer.text = timeString
            }

            override fun onFinish() {
                binding.countDownTimer.text =  "Event is Live Now"
            }

        }
        countDownTimer.start()
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
                event = it.event[0]
                Log.w("sebastian","event")
                setCountDownTimer(event.start_time)
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
    private fun getFormattedTime(millisUntilFinished: Long): String {
        val days = millisUntilFinished / (1000 * 60 * 60 * 24)
        val hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millisUntilFinished % (1000 * 60)) / 1000

        return String.format(
            "%04d:%02d:%02d:%02d:%02d",
            days, hours, minutes, seconds
        )
    }
}