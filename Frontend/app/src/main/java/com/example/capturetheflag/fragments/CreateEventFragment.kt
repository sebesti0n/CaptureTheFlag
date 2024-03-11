package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentCreateEventBinding
import com.example.capturetheflag.databinding.LayoutQuestionDialogBinding
import com.example.capturetheflag.models.EventX
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.ui.CreateEventViewModel
import com.example.capturetheflag.util.QuestionAdapter
import com.example.capturetheflag.util.QuestionItemClickListener
import com.example.capturetheflag.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
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
    private lateinit var storage:FirebaseStorage
    private lateinit var storageRef:StorageReference
    private var posterUri:Uri?=null
    private var flagCount = 0
    private var eid = -1
    private var getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        if(it!=null){
            binding.EventPoster.setImageURI(it)
            posterUri=it
        }
    }
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
        setupFirebaseStorage()
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

    private fun setupFirebaseStorage() {
        storage=FirebaseStorage.getInstance()
        storageRef = storage.reference
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
            Snackbar.make(requireView(),"Fill all the details!",2000).show()
        } else {
            flagCount = flgCnt.toInt()
            if (problemList.size == 0) {
                var imageurl = ""
                lifecycleScope.launch(Dispatchers.Main) {
                    val imgurl = lifecycleScope.async {
                        getimageurl()
                    }
                    imageurl = imgurl.await()
                    Log.w("sebastian Poster", imageurl)
                    val mEvent = EventX(
                        flagCount,
                        des,
                        "$endDate $endTime",
                        org,
                        location,
                        viewModel.getUID(),
                        imageurl,
                        "$stDate $strtTime",
                        title
                    )
                    Log.w("sebastian Poster", mEvent.toString())
                    viewModel.createEvent(mEvent)
                    viewModel.get()?.observe(requireActivity()) {
                        when(it){
                            is Resource.Success -> {
                                eid = it.data!!.event[0].event_id
                                addQuestionDialog()
                            }
                            is Resource.Error -> {
                                showToast(it.message!!)
                            }
                            else -> {
                                showToast("Loading")
                            }
                        }
                    }
                }
            }
                if (problemList.size == flagCount) {
                    viewModel.addTasks(problemList)
                    val action =
                        CreateEventFragmentDirections.actionCreateEventFragmentToFirstFragment()
                    findNavController().navigate(action)
                }
            }

    }

    private suspend fun getimageurl(): String {
        return suspendCoroutine { continuation ->
                if (posterUri != null) {
                    val imageRef = storageRef.child("/images/${posterUri!!.lastPathSegment}")
                    val uploadTask = imageRef.putFile(posterUri!!)
                    uploadTask.addOnFailureListener { exception ->
                        // Handle failure
                        continuation.resumeWith(Result.failure(exception))
                    }.addOnSuccessListener { _ ->
                        imageRef.downloadUrl.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                val imageUrl = downloadUri.toString()
                                continuation.resume(imageUrl)
                                showToast("Upload successful! URL: $imageUrl")
                                Log.d("seb_IMG_erl", imageUrl)
                            } else {
                            showToast("Failed to get download URL")
                            Log.w("seb_IMG_DWNLOAD", "getDownloadUrlTask:failure", task.exception)
                                continuation.resumeWith(Result.failure(Exception("Failed to get download URL")))
                            }
                        }
                    }
                } else {
                    // Handle no posterUri
                    continuation.resumeWith(Result.failure(Exception("No posterUri provided")))
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
                val question = QuestionModel(eid ,quesString, correctAnswer, unqCode)
                problemList.add(question)
                qAdapter.notifyDataSetChanged()
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
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(INPUT_MODE_CLOCK)
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
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setInputMode(INPUT_MODE_CLOCK)
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
    private fun uploadPoster() {
        getContent.launch("image/*")
    }
    override fun onQuestionClickListner(ques: QuestionModel) {
        showRiddleDialog(ques)
    }
    private fun showRiddleDialog(currQues:QuestionModel){
        val builder = MaterialAlertDialogBuilder(requireActivity())
            .setMessage("Q$- ${currQues.question}\nAns- ${currQues.answer}\nCode- ${currQues.unique_code}")
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Okay") { _, _ ->
            }
        val dialog = builder.create()
        dialog.show()
    }
    private fun showToast(msg:String){
        Toast.makeText(requireContext(),msg,Toast.LENGTH_SHORT).show()
    }

}