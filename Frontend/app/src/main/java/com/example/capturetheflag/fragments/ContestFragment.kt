package com.example.capturetheflag.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.util.TimeLineAdapter
import com.example.capturetheflag.databinding.FragmentContestBinding
import com.example.capturetheflag.helper.PermissionHelper
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.models.RiddleModel
import com.example.capturetheflag.room.CtfDatabase
import com.example.capturetheflag.ui.ContestViewModel
import com.example.capturetheflag.util.PermissionListener
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch

class ContestFragment : Fragment(),PermissionListener{
    private var _binding:FragmentContestBinding?=null
    private val binding get() = _binding!!
    private val args:ContestFragmentArgs by navArgs()
    private lateinit var viewModel: ContestViewModel
    private lateinit var permissionHelper: PermissionHelper
    private var isFirstAttempted = false
    private var riddleNumber:Int=0
    private var isCompleted = false
    private var eid =-1
    private lateinit var rList: List<RiddleModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eid = args.eid
        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
        _binding = FragmentContestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRoomDatabase(){ message, success ->
            if(success){
                rList = viewModel.getRiddles()
                updateUI()

            }
            else{
                showSnackbar(message!!)
                findNavController().popBackStack()
            }
        }
        binding.endButton.setOnClickListener {
            if(!isFirstAttempted){
                if(binding.etCorrectAnswer.text.toString().isEmpty()){
                    showSnackbar("Enter Answer")
                }
                else{
                    if(binding.etCorrectAnswer.text.toString() == rList[riddleNumber].answer){
                        isFirstAttempted = true
                        updateDescription()
                        binding.etCorrectAnswer.setText("")
                    }
                    else showSnackbar("Wrong Answer")
                }

            }
            else{
                if(binding.etCorrectAnswer.text.toString().isEmpty()){
                    showSnackbar("Enter Answer")
                }
                else{
                    if(binding.etCorrectAnswer.text.toString() == rList[riddleNumber].unique_code){
                        showSnackbar("question Completed")
                        viewModel.submitRiddleResponse(
                            eid = eid,
                            tid = viewModel.getTeamId(),
                            sumitAt = System.currentTimeMillis()
                        ){ success, message, nextRiddleNumber->
                            if(success!!){
                                riddleNumber = nextRiddleNumber!!
                                isFirstAttempted = false
                                updateUI()
                                viewModel.setLevel(nextRiddleNumber)
                            }
                            else{
                                showSnackbar(message!!)
                            }
                        }
                    }
                    else showSnackbar("Wrong Location")
                }
            }
        }

    }

    private fun updateUI() {
        riddleNumber = viewModel.getLevel()
        if(riddleNumber >= rList.size){
            binding.endButton.text = "Submit"
            isCompleted = true
        }
        else{
            updateDescription()
        }
    }

    private fun updateDescription() {
        if(!isFirstAttempted)
        binding.questionTv.text = rList[riddleNumber].question
        else binding.questionTv.text = rList[riddleNumber].storyline
    }

    private fun setupRoomDatabase(callback: (String?, Boolean)->Unit) {
        if(!viewModel.checkIfDataCached()){
            viewModel.onStartContest(
                eid = eid,
                rid = viewModel.getRegId(),
                startMs = System.currentTimeMillis()
            ){ success, message, state ->
                if(success!!){
                    if(state==null){
                        showSnackbar("state is null bitch")
                    }
                    else{
                        viewModel.cacheData(state.riddleModelList)
                        viewModel.setLevel(state.next.Number_correct_answer)
                        viewModel.createSession(
                            state.next.team_id,
                            state.next.Number_correct_answer
                        )
                        callback(null, true)

                    }
                }
                else {
                    callback(message, false)
                }

            }
        }
        else {
            callback(
                null,
                true
            )
        }
    }
    private fun showSnackbar(message: String){
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



    override fun shouldShowRationaleInfo() {
        permissionHelper.launchPermissionDialog(Manifest.permission.CAMERA)
    }

    override fun isPermissionGranted(isGranted: Boolean) {
        Log.w("sebastian scanResult","fun is permission granted")

        if(isGranted){
            Log.w("sebastian scanResult","granted")
            setupScanner()
        }
    }

}