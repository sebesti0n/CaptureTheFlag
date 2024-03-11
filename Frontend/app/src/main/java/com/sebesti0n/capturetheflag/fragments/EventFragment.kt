package com.sebesti0n.capturetheflag.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sebesti0n.capturetheflag.R
import com.sebesti0n.capturetheflag.activities.HomeActivity
import com.sebesti0n.capturetheflag.databinding.FragmentEventBinding
import com.sebesti0n.capturetheflag.databinding.LayoutTeamRegistrationFormBinding
import com.sebesti0n.capturetheflag.models.Event
import com.sebesti0n.capturetheflag.models.TeamSchema
import com.sebesti0n.capturetheflag.session.Session
import com.sebesti0n.capturetheflag.ui.EventViewModel
import com.sebesti0n.capturetheflag.util.EventType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private lateinit var dialogBinding: LayoutTeamRegistrationFormBinding
    private val args: EventFragmentArgs by navArgs()
    private lateinit var dialog: BottomSheetDialog
    private lateinit var viewModel: EventViewModel
    private val binding get() = _binding!!
    private lateinit var session: Session
    private lateinit var event: Event
    private var isRegister = false
    private var eid: Long = -1
    private var isLive = false
    private var eventType = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        eid = args.eid
        isLive = args.isLive
        session = Session.getInstance(requireContext())
        updateUI()
        hideBottomNavigationBar()
//        hideRegisterButton()
        initializeTeamRegistrationBottomSheetDialog()
        binding.btnRegisteredEvent.setOnClickListener {
            if (!isRegister) {
                if (eventType == EventType.TEAM_EVENT)
                    registerUserForEvent() {
                        if(it) moveToContestFragment()
                        else Snackbar.make(requireView(),"Team Registration Failed",2000).show()
                    }
                else if (eventType == EventType.INDIVIDUAL_EVENT)
                    registerIndividuallyEvent(){
                        if(it){
                            Snackbar.make(
                                requireView(),
                                "Registered Successfully",
                                2000)
                                .show()
                        }
                        else{
                            Snackbar.make(
                                requireView(),
                                "Registration Failed",
                                2000)
                                .show()
                        }
                    }

            }
            else{
                if(isLive){
                    moveToContestFragment()
                }
            }
        }

    }

    private fun moveToContestFragment() {
        if (isLive) {
            val action = EventFragmentDirections.actionEventFragmentToContestFragment(eid.toInt())
            findNavController().navigate(action)
        }

    }

    private fun hideBottomNavigationBar() {
        val homeActivity = activity as HomeActivity
        homeActivity.hideNavigation()
    }

    private fun hideRegisterButton() {
        if (eventType == EventType.NO_REGISTRATION_EVENT) {
            binding.btnRegisteredEvent.visibility = View.GONE
        }

    }

    private fun registerIndividuallyEvent(callback: (Boolean) -> Unit) {

        val newTeam = TeamSchema(
            eid.toInt(),
            session.getEmail(),
            session.getEnrollmentID(),
            session.getUserName(),
            "",
            "",
            "",
            "",
            session.getCollege(),
            session.getMobile()
        )
        registerTeamForEvent(newTeam) {
            callback(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun registerUserForEvent(callback: (Boolean) -> Unit) {
        teamRegistrationForm() {
            callback(it)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        if (eid.toInt() == -1) {
            binding.contentDescription.text = "Not found"
        } else {
            viewModel.eventDetails(eid.toInt()) { it, error ->
                if (error == true) {
                    Snackbar.make(requireView(), "Server Error", 2000).show()
                } else {
                    if (it != null) {
                        if (!it.success) {
                            Snackbar.make(requireView(), "Server Error", 2000)
                                .show()
                        } else {
                            event = it.event[0]
                            isRegister = it.isRegister
                            eventType = event.event_type
                            Log.w("sebastian", "event")
                            if(eventType==EventType.NO_REGISTRATION_EVENT)
                                binding.btnRegisteredEvent.visibility=View.GONE
                            setCountDownTimer(event.start_time, event.end_time)
                            binding.tvTitle.text = event.title
                            binding.contentDescription.text = event.description
                            val imgview = binding.banner
                            checkIfFragmentAttached {
                                Glide.with(requireContext())
                                    .load(event.posterImage)
                                    .into(imgview)
                            }
                        }
                    }
                }
            }
        }
        if (isRegister && !isLive) {
            binding.btnRegisteredEvent.visibility = View.INVISIBLE
        } else if (isLive && isRegister) {
            binding.btnRegisteredEvent.visibility = View.VISIBLE
            binding.btnRegisteredEvent.text = "Start"
        } else {
            if (isLive) {
                binding.btnRegisteredEvent.text = "Start"
                binding.btnRegisteredEvent.visibility = View.VISIBLE
            }
        }
    }

    private fun initializeTeamRegistrationBottomSheetDialog() {
        dialog = BottomSheetDialog(requireContext())
        dialogBinding = LayoutTeamRegistrationFormBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun teamRegistrationForm(callback: (Boolean) -> Unit) {
        dialog.show()
        dialogBinding.etPlayer1EID.isFocusable = false
        dialogBinding.etPlayer1EID.setText(viewModel.enrollID)
        dialogBinding.btnLogin.setOnClickListener {
            val teamName = dialogBinding.etTeamName.text.toString()
            val player1Name = dialogBinding.etPlayer1Name.text.toString()
            val player2Name = dialogBinding.etPlayer2Name.text.toString()
            val player3Name = dialogBinding.etPlayer3Name.text.toString()
            val player2EID = dialogBinding.etPlayer2EID.text.toString()
            val player3EID = dialogBinding.etPlayer3EID.text.toString()
            val leaderEmail = dialogBinding.etLeaderEmail.text.toString()
            val waNumber = dialogBinding.etWaNumber.text.toString()

            if (teamName.isEmpty() || player1Name.isEmpty()  || player2EID.isEmpty() ||
                player3EID.isEmpty() || player2Name.isEmpty() ||
                player3Name.isEmpty() || leaderEmail.isEmpty() ||
                waNumber.isEmpty()
            ) {
                Snackbar.make(requireView(), "Kindly Fill all Details", 2000).show()
            } else {
                val newTeam = TeamSchema(
                    eid.toInt(),
                    leaderEmail,
                    viewModel.enrollID,
                    player1Name,
                    player2EID,
                    player2Name,
                    player3EID,
                    player3Name,
                    teamName,
                    waNumber
                )
                registerTeamForEvent(newTeam) {
                    if (it) {
                        dialog.dismiss()
                        callback(true)

                    } else {
                        callback(false)
                    }
                }
            }
        }


    }

    private fun registerTeamForEvent(newTeam: TeamSchema, callback: (Boolean) -> Unit) {
        viewModel.registerTeamForEvent(newTeam) { success, message ->
            if (success == true) {
                isRegister = true
                callback(true)
            } else {
                Snackbar.make(requireView(), message.toString(), 2000).show()
                callback(false)
            }
        }
    }

    private fun setCountDownTimer(dateString: String, endTime: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        try {
            var targetDate = dateFormat.parse(dateString)
            val currentDate = Date()

            var timeDifference = targetDate!!.time - currentDate.time

            if (timeDifference > 0) {
                val countDownTimer = object : CountDownTimer(timeDifference, 1000L) {
                    override fun onTick(millisUntilFinished: Long) {
                        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                        val countdownTime = String.format("%02d:%02d:%02d", days, hours, minutes, seconds)

                        binding.countDownTimer.text ="Starts in ${countdownTime}"
                    }

                    override fun onFinish() {
                        Snackbar.make(requireView(), "start contest", 2000).show()
                    }
                }
                countDownTimer.start()
            } else {
                targetDate = dateFormat.parse(endTime)
                if (targetDate != null) {
                    timeDifference = targetDate.time - currentDate.time
                }
                val countDownTimer = object : CountDownTimer(timeDifference, 1000L) {
                    @SuppressLint("SetTextI18n")
                    override fun onTick(millisUntilFinished: Long) {
                        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

                        binding.countDownTimer.text =
                            "Ends in "+String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
                    }
                    override fun onFinish() {
                        Snackbar.make(requireView(), "contest is ended", 2000)
                            .show()
                    }
                }
                countDownTimer.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun checkIfFragmentAttached(next: Context.()->Unit){
        val context = requireContext()
        context?.let{
            next(it)
        }
    }
}