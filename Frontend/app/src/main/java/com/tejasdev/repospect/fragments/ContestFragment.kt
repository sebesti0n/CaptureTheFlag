package com.tejasdev.repospect.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tejasdev.repospect.R
import com.tejasdev.repospect.databinding.FragmentContestBinding
import com.tejasdev.repospect.helper.PermissionHelper
import com.tejasdev.repospect.models.RiddleModel
import com.tejasdev.repospect.ui.ContestViewModel
import com.tejasdev.repospect.util.PermissionListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import java.lang.StringBuilder
import kotlin.random.Random

class ContestFragment : Fragment(), PermissionListener {
    private var _binding: FragmentContestBinding? = null
    private val binding get() = _binding!!
    private val args: ContestFragmentArgs by navArgs()
    private lateinit var viewModel: ContestViewModel
    private lateinit var permissionHelper: PermissionHelper
    private var eid = -1
    private lateinit var rList: List<RiddleModel>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isPermissionsGranted = false
    private var firstPartAnswer=""
    private lateinit var memeList:ArrayList<Int>
    private lateinit var animator: ObjectAnimator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eid = args.eid
        _binding = FragmentContestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRefreshAnimator()

        permissionHelper = PermissionHelper(this, this)
        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
        memeList = ArrayList()
        makeMemeArray()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkPermissions()
        showProgressBar()
        setupRoomDatabase() { message, success ->
            if (success) {
                rList = viewModel.getRiddles()
            } else {
                showSnackbar(message!!)
                findNavController().popBackStack()
            }
        }
        updateQuestionState()
        binding.refreshButton.setOnClickListener {
            showProgressBar()
//            startAnimationOnRefreshButton()
            refreshAction {

              //  stopAnimationOnRefreshButton()
            }
        }


        binding.endButton.setOnClickListener {
            val answer = binding.etCorrectAnswer.text.toString()
            val index = viewModel.questionState.value!![0]
            val part = viewModel.questionState.value!![1]
            showProgressBar()
            if (index == viewModel.getRiddles().size) {
                endContest()
            } else if (part == 1) {
                checkWithStoryLine(index, answer) { it, msg ->
                    if (it) {
                        viewModel.submissionRiddleResponse(
                            eid = eid,
                            tid = viewModel.getTeamId(),
                            currRid = viewModel.getRiddles()[index].question_id,
                            nextRid = if (index + 1 == viewModel.getRiddles().size) -1
                            else viewModel.getRiddles()[index + 1].question_id,
                            unqCode = viewModel.getRiddles()[index].unique_code,
                            answer = viewModel.getRiddles()[index].answer
                        ) { success, message, nextRiddleNumber ->
                            Log.d(
                                "sebasti0n riddle submission",
                                "${success} + ${message} + ${nextRiddleNumber}"
                            )
                            if (success!! && nextRiddleNumber != -1) {
                                viewModel.questionState.postValue(
                                    arrayOf(nextRiddleNumber!!, 0)
                                )
                                viewModel.setLevel(nextRiddleNumber)
                                binding.etCorrectAnswer.setText("")
                                binding.etUniqueCode.setText("")
                            } else {
                                showSnackbar(message!!)
                                showMemeDialog(message)
                                hideProgressBar()
                                binding.apply {
                                    tilCorrectAnswer.visibility = View.GONE
                                    tilUnqCode.visibility = View.VISIBLE
                                    fabScan.visibility = View.VISIBLE
                                    llHints.visibility = View.GONE
                                }
                            }
                        }
                    } else {
                        hideProgressBar()
                        showMemeDialog(msg)
                        binding.apply {
                            tilCorrectAnswer.visibility = View.GONE
                            tilUnqCode.visibility = View.VISIBLE
                            fabScan.visibility = View.VISIBLE
                            llHints.visibility = View.GONE
                        }
                        if (msg != null) {
//                            showSnackbar(msg)
                        }
                    }

                }
            } else {
                if (checkWithQuestion(index, answer)) {
                    viewModel.questionState.postValue(
                        arrayOf(index, 1)
                    )
                    firstPartAnswer = binding.etCorrectAnswer.text.toString()
                    binding.etCorrectAnswer.setText("")
                    binding.etUniqueCode.setText("")
                } else {
                    showMemeDialog("Wrong answer. Try again!")
                    hideProgressBar()
                    binding.apply {
                        tilCorrectAnswer.visibility = View.VISIBLE
                        tilUnqCode.visibility = View.GONE
                        fabScan.visibility = View.GONE
                        llHints.visibility = View.VISIBLE

                    }
                }
            }
            binding.fabScan.setOnClickListener {
                if (isPermissionsGranted) {
                    setupScanner()
                } else {
                    showSnackbar("Please Grant Permissions")
                    checkPermissions()
                }
            }
        }
            binding.hint1Card.setOnClickListener {
                openHintDialog(hintType = 1)
            }
            binding.hint2Card.setOnClickListener {
                openHintDialog(2)
            }
            binding.hint3Card.setOnClickListener {
                openHintDialog(3)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.closeCtfSession()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.closeCtfSession()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.closeCtfSession()
    }


    private fun showMemeDialog(msg: String?) {
        val dialogBuilder = AlertDialog.Builder(requireContext(),R.style.AlertDialogTheme)
        val dialogView = layoutInflater.inflate(R.layout.layout_meme_dialog, null)
        val meme_id = Random.nextInt(memeList.size)
        dialogBuilder.setView(dialogView)
        val drawable = ContextCompat.getDrawable(requireContext(), memeList[meme_id])
        val memeImg = dialogView.findViewById<ImageView>(R.id.iv_meme)
        val tvWrongMessage = dialogView.findViewById<TextView>(R.id.tv_wrong_answer_message)
        if(msg!=null) tvWrongMessage.text = msg
        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        memeImg.setImageDrawable(drawable)
        alertDialog.show()
    }
    private fun makeMemeArray(){
        memeList.add(R.drawable.meme_1)
        memeList.add(R.drawable.meme_2)
        memeList.add(R.drawable.meme_3)
        memeList.add(R.drawable.meme_4)
        memeList.add(R.drawable.meme_5)
        memeList.add(R.drawable.meme_6)
        memeList.add(R.drawable.meme_7)
        memeList.add(R.drawable.meme_8)
        memeList.add(R.drawable.meme_9)
        memeList.add(R.drawable.meme_10)
        memeList.add(R.drawable.meme_11)
        memeList.add(R.drawable.meme_12)
    }

    private fun setUpRefreshAnimator() {
        animator = ObjectAnimator.ofFloat(binding.refreshButton, "rotation", 0f, 360f)
            .setDuration(2500)
            .apply {
                repeatCount = 1
            }
    }

    private fun startAnimationOnRefreshButton(){
        if(!animator.isRunning)
            animator.start()
    }

    private fun openHintDialog(hintType: Int){
        val index = viewModel.questionState.value!![0]
        val riddle = viewModel.getRiddles()[index]
        val dialogBuilder = AlertDialog.Builder(requireContext(),R.style.AlertDialogTheme)
        val dialogView = layoutInflater.inflate(R.layout.layout_hint_dialog, null)
        dialogBuilder.setView(dialogView)

        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progress_bar_dialog)
        val contentTextView = dialogView.findViewById<TextView>(R.id.tv_content_hint)
        val btnDone= dialogView.findViewById<Button>(R.id.dialog_button)
        val heading :TextView = dialogView.findViewById(R.id.hint_heading)
        val divider: MaterialDivider = dialogView.findViewById(R.id.dialog_divider)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        progressBar.visibility = View.VISIBLE
        contentTextView.visibility = View.GONE
        heading.visibility = View.GONE
        divider.visibility = View.GONE
        var hint="Locked"
        handleHintStatus(hintType){success,message,UnlockedIn,hint1,hint2,hint3->
            if(success){
                if(UnlockedIn != 0L){
                    if (hintType==1){
                        hint = "Unlocked in ${convertMillisToTime(1800000L-UnlockedIn!!)}"
                    }else if(hintType == 2)hint = "Unlocked in ${convertMillisToTime(3600000L-UnlockedIn!!)}"
                    else hint = "Unlocked in ${convertMillisToTime(5400000L-UnlockedIn!!)}"
                }else if(hintType==1&&hint1){
                    hint = riddle.Hint1
                } else if(hintType==2&&hint2){
                    hint =riddle.Hint2
                } else if(hint3){
                    hint=riddle.Hint3
                }
                contentTextView.text = hint
                progressBar.visibility = View.GONE
                contentTextView.visibility = View.VISIBLE
                heading.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
            }
            else{
                showSnackbar(message!!)
            }
        }
        btnDone.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun convertMillisToTime(millis: Long): String {
        Log.w("sebesti0n millis",millis.toString());
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        val remainingSeconds = seconds % 60
        return String.format("%02dh %02dmin %02dsec", hours, remainingMinutes, remainingSeconds)

    }
    private fun updateQuestionState() {
        viewModel.questionState.observe(viewLifecycleOwner, Observer {
            val index = it[0]
            val question = it[1]
            if (index != -1) {
                hideProgressBar()
                updateUI(index, question)
            }
        })
    }
    private fun handleHintStatus(
        hintType:Int,
        callback: (Boolean,String?,Long?,Boolean,Boolean,Boolean) -> Unit
    ){
        val index = viewModel.questionState.value!![0]
        viewModel.getHintStatus(
            eventId = eid,
            teamId = viewModel.getTeamId(),
            riddleId = viewModel.getRiddles()[index].question_id,
            hintType
        ){ success,message,UnlockedIn,hint1,hint2,hint3->
            callback(success,message,UnlockedIn,hint1,hint2,hint3)
        }
    }
    private fun refreshAction(callback:()->Unit) {
        viewModel.closeCtfSession()
        setupRoomDatabase() { message, success ->
            if (success) {
                rList = viewModel.getRiddles()
                updateQuestionState()

            } else {
                showSnackbar(message!!)
                findNavController().popBackStack()
            }
        }
        callback()
    }

    private fun checkPermissions() {
        permissionHelper.checkForMultiplePermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun endContest() {
        val action = ContestFragmentDirections.actionContestFragmentToHomefragment()
        findNavController().navigate(action)
    }

    private fun checkWithQuestion(index: Int, answer: String): Boolean {
        return answer.lowercase().trim() == viewModel.getRiddles()[index].answer.lowercase().trim()
    }

    private fun checkWithStoryLine(index: Int, answer: String, callback: (Boolean,String?) -> Unit) {
        requestLocation(){success,lat,long->
            if (success){
                calculateDistance(lat!!,
                    long!!,
                    viewModel.getRiddles()[index].Latitude,
                    viewModel.getRiddles()[index].Longitude,
                    viewModel.getRiddles()[index].Range)
                {
                    if(it&&(answer == viewModel.getRiddles()[index].unique_code))
                    callback( true, "Correct")
                    else if(!it&&(answer == viewModel.getRiddles()[index].unique_code))
                        callback(false, "You are at wrong place")
                    else callback(false, "wrong answer")
                }
            }
            else{
                callback(false,"Location cannot detected")
            }
        }
    }

    private fun updateUI(index: Int, question: Int) {
        if (index == viewModel.getRiddles().size) {
            binding.fabScan.visibility=View.GONE
            binding.tilUnqCode.visibility = View.GONE
            binding.endButton.text = "End"
            binding.questionTv.text =
                "Click on the button below to end the contest! Thanks for participating."
            binding.apply {
                tilCorrectAnswer.visibility = View.GONE
                hint1Card.visibility =View.GONE
                hint2Card.visibility = View.GONE
                hint3Card.visibility = View.GONE
            }
            return
        }
        when (question) {
            1 -> {
                val storyline = StringBuilder().append(viewModel.getRiddles()[index].storyline)
                binding.questionTv.text = storyline
                binding.endButton.text = "Submit"
                binding.fabScan.visibility = View.VISIBLE
                binding.llHints.visibility=View.GONE
                binding.tilUnqCode.visibility = View.VISIBLE
                binding.tilCorrectAnswer.visibility = View.GONE
                val imgLink=viewModel.getRiddles()[index].imageLink
                checkIfFragmentAttached{
                    if (imgLink != "null") {
                        parentFragment?.let {
                            Glide.with(it)
                                .load(imgLink)
                                .into(binding.image)
                        }
                    } else {
                        binding.image.visibility = View.INVISIBLE
                    }
                }
            }

            else -> {
                binding.questionTv.text = viewModel.getRiddles()[index].question
                binding.endButton.text = "Next"
                binding.fabScan.visibility = View.GONE
                binding.llHints.visibility=View.VISIBLE
                binding.tilUnqCode.visibility = View.GONE
                binding.tilCorrectAnswer.visibility = View.VISIBLE
                val imgLink=viewModel.getRiddles()[index].riddleImageLink
                checkIfFragmentAttached{
                    if (imgLink != "null" && imgLink.isNotEmpty()) {
                        parentFragment?.let {
                            Glide.with(it)
                                .load(imgLink)
                                .into(binding.image)
                        }
                    } else {
                        binding.image.visibility = View.INVISIBLE
                    }
                }
            }

        }
    }

    private fun setupRoomDatabase(callback: (String?, Boolean) -> Unit) {
        if (!viewModel.checkIfDataCached()) {
            viewModel.onStartContest(
                eid = eid,
                rid = viewModel.getRegId(),
                startMs = System.currentTimeMillis()
            ) { success, message, state ->
                if (success!!) {
                    if (state == null) {
                        showSnackbar("state is null bitch")
                        callback("something went wrong", false)
                    } else {
                        viewModel.cacheData(state.riddleModelList)
                        viewModel.setLevel(state.next.Number_correct_answer)
                        viewModel.createSession(
                            state.next.team_id,
                            state.next.Number_correct_answer
                        )
                        viewModel.questionState.postValue(
                            arrayOf(state.next.Number_correct_answer, 0)
                        )
                        callback(null, true)
                    }
                } else {
                    callback(message, false)
                }

            }
        } else {
            viewModel.questionState.postValue(
                arrayOf(viewModel.getLevel(), 0)
            )
            callback(
                null,
                true
            )
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            requireView(),
            message,
            2000
        ).show()
    }

    private fun setupScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt(" QR code should be inside square box ")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {

            if (scanResult.contents == null) {
                Snackbar.make(requireView(), "Cancelled", 2000).show();
            } else {
                binding.etCorrectAnswer.setText(scanResult.contents.toString())
                binding.etUniqueCode.setText("......")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            image.visibility = View.GONE
            tilCorrectAnswer.visibility = View.GONE
            questionTv.visibility = View.GONE
            endButton.visibility = View.GONE
            hint1Card.visibility =View.GONE
            hint2Card.visibility = View.GONE
            hint3Card.visibility = View.GONE
            fabScan.visibility = View.GONE
            tilUnqCode.visibility = View.GONE
            refreshButton.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            image.visibility = View.VISIBLE
            questionTv.visibility = View.VISIBLE
            endButton.visibility = View.VISIBLE
            hint1Card.visibility =View.VISIBLE
            hint2Card.visibility = View.VISIBLE
            hint3Card.visibility = View.VISIBLE
            refreshButton.visibility = View.VISIBLE
        }
    }

    override fun shouldShowRationaleInfo() {
        permissionHelper.launchPermissionDialogForMultiplePermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            isPermissionsGranted=true
        } else permissionHelper.launchPermissionDialogForMultiplePermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun requestLocation(callback: (Boolean, Double?, Double?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionHelper.checkForMultiplePermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            permissionHelper.launchPermissionDialogForMultiplePermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            callback(false,null,null)
            return
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.w("Contest Fragment Location", "Latitude: $latitude, Longitude: $longitude")
                    callback(true,latitude,longitude)
                } else {
                    callback(false,null,null)
                    showSnackbar("Please Turn on your GPS")
                }
            }
            .addOnFailureListener { e ->
                callback(false,null,null)
                showSnackbar("Failed to get location: ${e.message}")
            }
    }
    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double,
        range: Int,
        callback: (Boolean) -> Unit
    ){
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = StrictMath.sin(dLat / 2) * StrictMath.sin(dLat / 2) +
                StrictMath.cos(Math.toRadians(lat1)) * StrictMath.cos(Math.toRadians(lat2)) *
                StrictMath.sin(dLon / 2) * StrictMath.sin(dLon / 2)
        val c = 2 * StrictMath.atan2(StrictMath.sqrt(a), StrictMath.sqrt(1 - a))
        callback(R * c * 1000<=range)


    }
    private fun checkIfFragmentAttached(next: Context.()->Unit){
        val context = requireContext()
        context?.let{
            next(it)
        }
    }

}