package com.example.capturetheflag.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.capturetheflag.databinding.FragmentContestBinding
import com.example.capturetheflag.helper.DistanceFinder
import com.example.capturetheflag.helper.PermissionHelper
import com.example.capturetheflag.models.RiddleModel
import com.example.capturetheflag.ui.ContestViewModel
import com.example.capturetheflag.util.PermissionListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator

class ContestFragment : Fragment(), PermissionListener {
    private var _binding: FragmentContestBinding? = null
    private val binding get() = _binding!!
    private val args: ContestFragmentArgs by navArgs()
    private lateinit var viewModel: ContestViewModel
    private lateinit var permissionHelper: PermissionHelper
    private var eid = -1
    private lateinit var rList: List<RiddleModel>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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
        Log.w("ContestFragment","1")
        permissionHelper = PermissionHelper(this, this)
        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Log.w("ContestFragment","2")
        checkPermissions()
        Log.w("ContestFragment","3")
        showProgressBar()
        Log.w("ContestFragment","4")
        setupRoomDatabase() { message, success ->
            Log.w("ContestFragment","5")

            if (success) {
                Log.w("ContestFragment","51")

                rList = viewModel.getRiddles()
            } else {
                Log.w("ContestFragment","52")

                showSnackbar(message!!)
                findNavController().popBackStack()
            }
        }
        Log.w("ContestFragment","6")

        updateQuestionState()
        Log.w("ContestFragment","7")
        binding.swipeRefresLayout.setOnRefreshListener {
            Log.w("ContestFragment","8")
            refreshAction {
                Log.w("ContestFragment","9")
            }
            binding.swipeRefresLayout.isRefreshing = false
        }

        binding.endButton.setOnClickListener {
            val answer = binding.etCorrectAnswer.text.toString()
            val index = viewModel.questionState.value!![0]
            val part = viewModel.questionState.value!![1]
            showProgressBar()
            if (index == viewModel.getRiddles().size) {
                endContest()
            } else if (part == 1) {
                if (checkWithStoryLine(index, answer)) {
                    viewModel.submitRiddleResponse(
                        eid = eid,
                        tid = viewModel.getTeamId(),
                        sumitAt = System.currentTimeMillis()
                    ) { success, message, nextRiddleNumber ->
                        if (success!!) {
                            viewModel.questionState.postValue(
                                arrayOf(nextRiddleNumber!!, 0)
                            )
                            viewModel.setLevel(nextRiddleNumber)
                            binding.etCorrectAnswer.setText("")
                        } else {
                            showSnackbar(message!!)
                            hideProgressBar()
                        }

                    }
                } else {
                    hideProgressBar()
                    showSnackbar("Wrong Answer")
                }
            } else {
                if (checkWithQuestion(index, answer)) {
                    viewModel.questionState.postValue(
                        arrayOf(index, 1)
                    )
                    binding.etCorrectAnswer.setText("")
                } else {
                    hideProgressBar()
                    showSnackbar("Wrong Answer")
                }
            }
        }
    }

    private fun updateQuestionState() {
        Log.w("ContestFragment","13")
        viewModel.questionState.observe(viewLifecycleOwner, Observer {
            Log.w("ContestFragment","13")
            val index = it[0]
            val question = it[1]
            if (index != -1) {
                Log.w("ContestFragment","13-1")
                hideProgressBar()
                updateUI(index, question)
            }
        })
    }

    private fun refreshAction(callback:()->Unit) {
        Log.w("ContestFragment","10")
        viewModel.closeCtfSession()
        setupRoomDatabase() { message, success ->
            Log.w("ContestFragment","11")
            if (success) {
                Log.w("ContestFragment","11-1")
                rList = viewModel.getRiddles()
                updateQuestionState()

            } else {
                Log.w("ContestFragment","11-2")
                showSnackbar(message!!)
                findNavController().popBackStack()
            }
            Log.w("ContestFragment","12")
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
        return answer == viewModel.getRiddles()[index].answer
    }

    private fun checkWithStoryLine(index: Int, answer: String): Boolean {
        var inRange = false
        requestLocation(){success,lat,long->
            Log.w("Contest Fragment Location Callback",success.toString())
            if (success){
                Log.w("Contest Fragment Distance","before callback fun of calc Distace: ${inRange}")
                calculateDistance(lat!!,long!!,lat,long){
                    inRange = it
                }
                Log.w("Contest Fragment Distance","after callback fun of calc Distace: ${inRange}")
                showSnackbar("You too Far From Scanner. First Reach the Location then Scan QR")
            }
        }
        val flg = (answer == viewModel.getRiddles()[index].unique_code) and inRange
        Log.w("contestFragment Flag", flg.toString())
        return flg
    }

    private fun updateUI(index: Int, question: Int) {
        Log.w("ui-issue", "$index, $question, ${viewModel.getRiddles()}")
        if (index == viewModel.getRiddles().size) {
            binding.endButton.text = "End"
            binding.questionTv.text =
                "Click on the button below to end the contest! Thanks for participating."
            binding.tilCorrectAnswer.visibility = View.GONE
            return
        }
        when (question) {
            1 -> {
                binding.questionTv.text = viewModel.getRiddles()[index].storyline
                binding.endButton.text = "Scan"
            }

            else -> {
                binding.questionTv.text = viewModel.getRiddles()[index].question
                binding.endButton.text = "Next"
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
        integrator.setPrompt("scan")
        integrator.setCameraId(0)
        integrator.setOrientationLocked(true)
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
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
//                binding.etCode.setText(scanResult.contents.toString())
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
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            image.visibility = View.VISIBLE
            tilCorrectAnswer.visibility = View.VISIBLE
            questionTv.visibility = View.VISIBLE
            endButton.visibility = View.VISIBLE
        }
    }
    private fun showView() {
        binding.apply {
            progressBar.visibility = View.GONE
            image.visibility = View.GONE
            tilCorrectAnswer.visibility = View.GONE
            questionTv.visibility = View.GONE
            endButton.visibility = View.GONE
        }
    }

    private fun hideView() {
        binding.apply {
            image.visibility = View.VISIBLE
            tilCorrectAnswer.visibility = View.VISIBLE
            questionTv.visibility = View.VISIBLE
            endButton.visibility = View.VISIBLE
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
        Log.w("sebastian scanResult", "fun is permission granted")

        if (isGranted) {
            showSnackbar("Permission Granted")
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
                    showSnackbar("Latitude: $latitude, Longitude: $longitude")
                } else {
                    callback(false,null,null)
                    showSnackbar("Location is null")
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
        callback: (Boolean) -> Unit
    ){
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = StrictMath.sin(dLat / 2) * StrictMath.sin(dLat / 2) +
                StrictMath.cos(Math.toRadians(lat1)) * StrictMath.cos(Math.toRadians(lat2)) *
                StrictMath.sin(dLon / 2) * StrictMath.sin(dLon / 2)
        val c = 2 * StrictMath.atan2(StrictMath.sqrt(a), StrictMath.sqrt(1 - a))
        callback(R * c * 1000<=200)


    }

}