package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capturetheflag.databinding.FragmentEventBinding
import com.example.capturetheflag.models.Event
import com.example.capturetheflag.ui.EventViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val args: EventFragmentArgs by navArgs()
    private lateinit var viewModel: EventViewModel
    private var eid: Long = -1
    private lateinit var event: Event
    private var isLive = false
    private var isRegister = false
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
        Log.w("sebastian idk", "sex")
        eid = args.eid
        isLive = args.isLive
        updateUI()
        binding.btnRegisteredEvent.setOnClickListener {
            if(!isRegister)fetchRegisterStatusforEvent()
            if(isLive){
                val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
                findNavController().navigate(action)
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun fetchRegisterStatusforEvent() {
        viewModel.registerUserForEvent(eid.toInt())
    }

    private fun setCountDownTimer(dateString: String, endTime: String) {
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

                        binding.countDownTimer.text =
                            String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
                    }

                    override fun onFinish() {
                        Toast.makeText(requireContext(), "start contest", Toast.LENGTH_SHORT).show()
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

                        binding.countDownTimer.text =
                            String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
                    }

                    override fun onFinish() {
                        Toast.makeText(requireContext(), "contest is ended", Toast.LENGTH_SHORT)
                            .show()
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
        if (eid.toInt() == -1) {
            binding.contentDescription.text = "Not found"
            binding.contentDetails.text = "Not Found"
            binding.contentPrizes.text = "Not Found"
        } else {
            viewModel.eventDetails(eid.toInt()) { it, error ->
                if (error == true) {
                    Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
                } else {
                    if (it != null) {
                        if (!it.success) {
                            Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            event = it.event[0]
                            isRegister = it.isRegister
                            Log.w("sebastian", "event")
                            setCountDownTimer(event.start_time, event.end_time)
                            binding.contentDescription.text = event.description
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
        }
        if (isRegister&&!isLive){
            binding.btnRegisteredEvent.visibility = View.INVISIBLE
        }
        else if(isLive&&isRegister){
            binding.btnRegisteredEvent.visibility = View.VISIBLE
            binding.btnRegisteredEvent.text = "Start"
        }else{
            if(isLive){
                binding.btnRegisteredEvent.text = "Start"
                binding.btnRegisteredEvent.visibility = View.VISIBLE
            }
        }
    }
}