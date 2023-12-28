package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentCreateEventBinding
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.ui.CreateEventViewModel
import com.example.capturetheflag.util.QuestionAdapter
import com.example.capturetheflag.util.QuestionItemClickListner
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import kotlin.random.Random


class CreateEventFragment : Fragment(),QuestionItemClickListner {
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
    private lateinit var qAdapter:QuestionAdapter
    private lateinit var listner: QuestionItemClickListner
    private var posterUri:Uri?=null
    private var flagCount = 0
//    private var itr = 0
//    private late init var decodeBase64ImageTask: DecodeBase64ImageTask
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    listner = this
    }
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
        setUpQuestionRecyclerView()
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
                if (problemList.size<flagCount)
                addQuestionDialog()
                else{
                    binding.btnAddQuestion.setText("Submit")
                    Toast.makeText(requireContext(),"submit it",Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun setUpQuestionRecyclerView() {
        qAdapter = QuestionAdapter(problemList,listner)
        binding.rvQuestionList.adapter = qAdapter
        binding.rvQuestionList.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("MissingInflatedId")
    private fun addQuestionDialog() {
                val qNo = (problemList.size+1).toString()
                val headingText = "Add Question $qNo"
                    val dialogLayout = layoutInflater.inflate(R.layout.layout_question_dialog, null)
        dialogLayout.findViewById<TextView>(R.id.heading).text = headingText
                    val etQuestion = dialogLayout.findViewById<TextInputEditText>(R.id.et_ques)
                    val etAnswer = dialogLayout.findViewById<TextInputEditText>(R.id.et_correctAnswer)
                    val etUniqueCode = dialogLayout.findViewById<TextInputEditText>(R.id.et_uniqueCode)
                    val btn = dialogLayout.findViewById<MaterialButton>(R.id.btn_generateCode)
                    btn.setOnClickListener{
                        val code = generateRandomCode(6)
                        etUniqueCode.setText(code)
                    }
                    val builder = MaterialAlertDialogBuilder(requireActivity())
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
                                qAdapter.notifyDataSetChanged()
                            } else {
                                Toast.makeText(requireActivity(), "Fill all the details!", Toast.LENGTH_LONG).show()
                            }
                        }

                    val dialog = builder.create()
                    dialog.show()
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

    override fun onQuestionClickListner(ques: QuestionModel) {
        showQuestionDialog(ques)
    }
    private fun showQuestionDialog(currQues:QuestionModel){

        val builder = MaterialAlertDialogBuilder(requireActivity())
            .setMessage("Q$- ${currQues.question}\nAns- ${currQues.cAnswer}\nCode- ${currQues.unqCode}")
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancellation if needed
            }
            .setPositiveButton("Okay") { _, _ ->

            }

        val dialog = builder.create()
        dialog.show()
    }


}