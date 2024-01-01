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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentCreateEventBinding
import com.example.capturetheflag.databinding.LayoutQuestionDialogBinding
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.ui.CreateEventViewModel
import com.example.capturetheflag.util.ImageUtil
import com.example.capturetheflag.util.QuestionAdapter
import com.example.capturetheflag.util.QuestionItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random


class CreateEventFragment : Fragment(),QuestionItemClickListener {
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
    private lateinit var listner: QuestionItemClickListener
    private lateinit var dialogBinding: LayoutQuestionDialogBinding
    private lateinit var dialog : BottomSheetDialog
    private var posterUri:Uri?=null
    private var flagCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listner = this
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCreateEventBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMemberVariables()
        etStartDate.setOnClickListener {
            datePickerSetup()
        }
        etEndDate.setOnClickListener {
           endDatePicker()
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
            setupQuestionAddDialog()
        }
    }





    private fun setupQuestionAddDialog() {
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
            if(problemList.size==0){
                var base64Poster:String?=null
                if(posterUri!=null){
                    viewModel.viewModelScope.launch {
                        base64Poster =
                            ImageUtil.uriToBase64(requireContext().contentResolver, posterUri!!)
                    }
                }

                val mEvent = EventX(flagCount,des, "$endDate $endTime", org,location,1,base64Poster,"$stDate $strtTime",title)
//                viewModel.createEvent(mEvent)
                addQuestionDialog()
            }
            if(problemList.size==flagCount){
//                viewModel.addTasks(problemList)
                val action = CreateEventFragmentDirections.actionCreateEventFragmentToFirstFragment()
                findNavController().navigate(action)
            }
        }
    }


    private fun endDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(childFragmentManager, "Select Start Date")
        datePicker.addOnPositiveButtonClickListener {
            etEndDate.setText(datePicker.headerText)
        }
    }

    private fun datePickerSetup() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(childFragmentManager, "Select Start Date")
        datePicker.addOnPositiveButtonClickListener {
            etStartDate.setText(datePicker.headerText)
        }
    }

    private fun initializeMemberVariables() {
        viewModel = ViewModelProvider(this)[CreateEventViewModel::class.java]
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
        initializeBottomSheetDialog()
    }

    private fun initializeBottomSheetDialog() {
        dialog = BottomSheetDialog(requireContext())
        dialogBinding = LayoutQuestionDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.attributes?.windowAnimations  = R.style.DialogAnimation
    }

    private fun setUpQuestionRecyclerView() {
        qAdapter = QuestionAdapter(listner)
        binding.rvQuestionList.adapter = qAdapter
        qAdapter.setData(problemList)
        binding.rvQuestionList.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n", "NotifyDataSetChanged")
    private fun addQuestionDialog() {
        dialog.show()
        if(problemList.size+1==flagCount){
            dialogBinding.btnNext.text = "Add"
        }
        val qNo = (problemList.size+1).toString()
        val headingText = "Add Question $qNo"
        dialogBinding.heading.text = headingText
        val etQuestion = dialogBinding.etQues
        val etAnswer = dialogBinding.etCorrectAnswer
        val etUniqueCode = dialogBinding.etUniqueCode
        dialogBinding.btnGenerateCode.setOnClickListener{
            val code = generateRandomCode()
            etUniqueCode.setText(code)
        }
        dialogBinding.btnNext.setOnClickListener {
            val quesString = etQuestion.text.toString()
            val correctAnswer = etAnswer.text.toString()
            val unqCode = etUniqueCode.text.toString()
            if (quesString.isNotEmpty() && correctAnswer.isNotEmpty() && unqCode.isNotEmpty()) {
                val question = QuestionModel(1,quesString, correctAnswer, unqCode)
                problemList.add(question)
                qAdapter.notifyDataSetChanged()
//              qAdapter.addData(question)
                dialog.dismiss()
                Log.w("dialog","dismiss")
                if(problemList.size<flagCount){
                    etQuestion.setText("")
                    etAnswer.setText("")
                    etUniqueCode.setText("")
                    addQuestionDialog()
                } else {
                    binding.btnAddQuestion.text = "Submit"
                }

            } else {
                Toast.makeText(requireActivity(), "Fill all the details!", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun generateRandomCode(): String {
        val alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random.Default
        val code = StringBuilder()
        repeat(6) {
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
            .setMessage("Q$- ${currQues.question}\nAns- ${currQues.answer}\nCode- ${currQues.unique_code}")
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Okay") { _, _ ->
            }
        val dialog = builder.create()
        dialog.show()
    }

}