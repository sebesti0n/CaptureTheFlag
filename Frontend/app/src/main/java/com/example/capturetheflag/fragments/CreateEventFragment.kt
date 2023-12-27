package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentCreateEventBinding
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.ui.CreateEventViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import kotlin.random.Random


class CreateEventFragment : Fragment() {
    private var _binding:FragmentCreateEventBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateEventViewModel
    private lateinit var etTitle:TextInputEditText
    private lateinit var etOrganisation:TextInputEditText
    private lateinit var etDescription:TextInputEditText
    private lateinit var etLocation:TextInputEditText
    private lateinit var etPrizes:TextInputEditText
    private lateinit var etStartDate:TextInputEditText
    private lateinit var etStartTime:TextInputEditText
    private lateinit var etEndDate:TextInputEditText
    private lateinit var etEndTime:TextInputEditText
    private lateinit var etFlagCount:TextInputEditText
    private lateinit var problemList:ArrayList<QuestionModel>
    private var posterUri:Uri?=null
    private var flagCount = 0
    private var itr = 0



    //    private late init var decodeBase64ImageTask: DecodeBase64ImageTask

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentCreateEventBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)
        problemList = ArrayList()
        etTitle = binding.etTitle
        etDescription = binding.etDescription
        etLocation = binding.etLocation
        etOrganisation = binding.etLocation
        etPrizes = binding.etPrizes
        etStartDate = binding.etStartDate
        etEndDate = binding.etEndDate
        etStartTime = binding.etStartTime
        etEndTime = binding.etEndTime
        etFlagCount = binding.etFlagCount

        etStartDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "Select Start Date")
            datePicker.addOnPositiveButtonClickListener {
                etStartDate.setText(datePicker.headerText)
            }
        }
        etEndDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager, "Select Start Date")
            datePicker.addOnPositiveButtonClickListener {
                etEndDate.setText(datePicker.headerText)
            }
        }
        etStartTime.setOnClickListener {
         setStartTimePicker()
        }
        etEndTime.setOnClickListener {
            setEndTimePicker()
        }
        binding.EventPoster.setOnClickListener {
            uploadPoster()
        }
        binding.btnAddQuestion.setOnClickListener {
            val title = etTitle.text.toString()
            val des = etDescription.text.toString()
            val org = etOrganisation.text.toString()
            val location = etLocation.text.toString()
            val stDate = etStartDate.text.toString()
            val endDate = etEndDate.text.toString()
            val strtTime = etStartTime.text.toString()
            val endTime = etEndTime.text.toString()
            val flgCnt = etFlagCount.text.toString()
            if (title.isEmpty() || des.isEmpty() || org.isEmpty() || location.isEmpty() || stDate.isEmpty() || endDate.isEmpty() || strtTime.isEmpty() || endTime.isEmpty()||flgCnt.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all the details!", Toast.LENGTH_SHORT).show()
            } else {
                flagCount = flgCnt.toInt()
                itr = flagCount
                Log.w("sebastion","${itr.toString()} loop se pahele")
               addQuestionDialog(flagCount ,"Next")
                Log.w("Sebastion",problemList.size.toString())
                Log.w("Sebastion",problemList.toString())

            }
        }

    }
    var isDialogBoxShowing = true
    @SuppressLint("MissingInflatedId")
    private fun addQuestionDialog(n: Int, bt: String) {
        runBlocking {
            // Launching a coroutine to handle each question dialog
            for (i in 1..n) {
                val qNo = i.toString()
                val headingText = "Add Question $qNo"

                // Delay added for demonstration (simulating network call, etc.)
                delay(500) // Replace with your actual async task or remove this delay

                // Launching a coroutine to handle each question dialog
                launch(Dispatchers.Main) {
                    val dialogLayout = layoutInflater.inflate(R.layout.layout_question_dialog, null)
                    val etQuestion = dialogLayout.findViewById<TextInputEditText>(R.id.et_ques)
                    val etAnswer = dialogLayout.findViewById<TextInputEditText>(R.id.et_correctAnswer)
                    val etUniqueCode = dialogLayout.findViewById<TextInputEditText>(R.id.et_uniqueCode)
                    val btn = dialogLayout.findViewById<MaterialButton>(R.id.btn_generateCode)
                    btn.setOnClickListener{
                        etUniqueCode.setText("666666")
                    }
                    val builder = MaterialAlertDialogBuilder(requireActivity())
                        .setTitle(headingText)
                        .setView(dialogLayout)
                        .setNegativeButton("Cancel") { _, _ ->
                            // Handle cancellation if needed
                        }
                        .setPositiveButton("Submit") { _, _ ->
                            val quesString = etQuestion.text.toString()
                            val correctAnswer = etAnswer.text.toString()
                            val unqCode = etUniqueCode.text.toString()

                            if (quesString.isNotEmpty() && correctAnswer.isNotEmpty() && unqCode.isNotEmpty()) {
                                val question = QuestionModel(quesString, correctAnswer, unqCode)
                                problemList.add(question)
                            } else {
                                Toast.makeText(requireActivity(), "Fill all the details!", Toast.LENGTH_LONG).show()
                            }
                        }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }

    }
    private fun generateRandomCode(length: Int): String {
        val alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random.Default
        val code = StringBuilder()

        repeat(length) {
            val randomIndex = random.nextInt(alphanumericChars.length)
            code.append(alphanumericChars[randomIndex])
        }

        return code.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun setStartTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTimeText = "$selectedHour:$selectedMinute"
            etStartTime.setText(selectedTimeText)
        }
        timePicker.show(childFragmentManager, timePicker.toString())
    }
    private fun setEndTimePicker(){
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTimeText = "$selectedHour:$selectedMinute"
            etEndTime.setText(selectedTimeText)
        }
        timePicker.show(childFragmentManager, timePicker.toString())
    }
    private fun uploadPoster(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1&&resultCode==Activity.RESULT_OK&&data!=null){
            binding.EventPoster.setImageURI(data.data)
            posterUri=data.data
        }
    }
}