package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentEventBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.EventViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

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
//        val btnText = binding.btnRegisteredEvent.text.toString()
        binding.btnRegisteredEvent.setOnClickListener {
            val btnText = binding.btnRegisteredEvent.text.toString()
            if(btnText == "Start"){
                val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
                findNavController().navigate(action)
            }
        fetchRegisterStatusforEvent()
//            val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
//            findNavController().navigate(action)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun fetchRegisterStatusforEventOnOpen() {
        viewModel.getFirstStatus(eid.toInt())
        viewModel.onOpenStatus().observe(requireActivity()){
                binding.btnRegisteredEvent.text = it

        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchRegisterStatusforEvent() {
        viewModel.registerUserForEvent(eid.toInt())
        viewModel.getStatus().observe(requireActivity()) {
            binding.btnRegisteredEvent.text = it
        }

    }
    private fun setCountDownTimer(dateString:String,endTime:String){
//            val dateString = "2024-01-26T18:00:00.000Z"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

            try {
                var targetDate = dateFormat.parse(dateString)
                val currentDate = Date()

                var timeDifference = targetDate.time - currentDate.time

                if (timeDifference > 0) {
                    val countDownTimer = object : CountDownTimer(timeDifference, 1000L) {
                        override fun onTick(millisUntilFinished: Long) {
                            val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                            binding.countDownTimer.text=String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
                        }

                        override fun onFinish() {
                            Toast.makeText(requireContext(),"start contest",Toast.LENGTH_SHORT).show()
                        }
                    }

                    countDownTimer.start()
                } else {
                    targetDate = dateFormat.parse(endTime)
                    timeDifference = targetDate.time - currentDate.time
                    val countDownTimer = object : CountDownTimer(timeDifference, 1000L) {
                        override fun onTick(millisUntilFinished: Long) {
                            val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                            val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                            binding.countDownTimer.text=String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
                        }

                        override fun onFinish() {
                            Toast.makeText(requireContext(),"contest is ended",Toast.LENGTH_SHORT).show()
                        }
                    }

                    countDownTimer.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val includedLayoutView= activity?.findViewById<View>(R.id.app_bar_home)
        val bottomNavigationView = includedLayoutView?.findViewById<BottomNavigationView>(R.id.nav_bar)
        bottomNavigationView?.visibility = View.INVISIBLE
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
                setCountDownTimer(event.start_time,event.end_time)
//                binding.countDownTimer.text = event.start_time
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