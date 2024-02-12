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
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EventViewModel
    private var event: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            event = it.getParcelable("event")
        }
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.w("sebastian idk", "sex")
        if(event==null){
            showSnackBar("Some error occured")
            findNavController().popBackStack()
        }
        updateUI()
        binding.btnRegisteredEvent.setOnClickListener {
            if(!isRegister)registerUserForEvent()
            if(event.i){
                val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
                findNavController().navigate(action)
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun registerUserForEvent() {
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

    private fun showSnackBar(message: String){
        Snackbar.make(
            requireView(),
            message,
            2000
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        if (eid.toInt() == -1) {
            showSnackBar("Some error occured")
            findNavController().popBackStack()
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
                            binding.eventTitleTv.text = event.title
                            Log.w("sebastian", "event")
                            setCountDownTimer(event.start_time, event.end_time)
                            binding.eventDescTv.text = event.description
                            Glide.with(requireContext())
                                .load(event.posterImage)
                                .into(
                                    binding.eventBanner
                                )
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