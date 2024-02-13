package com.example.capturetheflag.fragments

import android.Manifest
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.TimeLineAdapter
import com.example.capturetheflag.databinding.FragmentContestBinding
import com.example.capturetheflag.helper.PermissionHelper
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.ui.ContestViewModel
import com.example.capturetheflag.util.PermissionListener
import com.google.zxing.integration.android.IntentIntegrator

class ContestFragment : Fragment(),PermissionListener{
    private var _binding:FragmentContestBinding?=null
    private val binding get() = _binding!!
    private val args:ContestFragmentArgs by navArgs()
    private lateinit var viewModel: ContestViewModel
    private lateinit var permissionHelper: PermissionHelper
    private var riddleNumber:Int=0
    private var eid =-1
    private lateinit var rList:ArrayList<QuestionModel>
    private lateinit var adapter: TimeLineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        adapter = TimeLineAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun shouldShowRationaleInfo() {
    }

    override fun isPermissionGranted(isGranted: Boolean) {
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        eid = args.eid
//        binding.riddleDiscription.text = "Not Found";
//        setupViewModel()
//        rList = ArrayList()
//        //getRiddlesList()
//        permissionHelper = PermissionHelper(this,this)
//        binding.btnScan.setOnClickListener {
//            Log.w("sebastian ","button clicked")
//            permissionHelper.checkPermissions(Manifest.permission.CAMERA)
//            setupScanner()
//        }
//        binding.submit.setOnClickListener {
//            handleOnClickofSubmitButton()
//        }
//
//    }
//
//    private fun handleOnClickofSubmitButton() {
//        val code = binding.etCode.text.toString()
//        if (code != rList[riddleNumber].unique_code) {
//            Toast.makeText(requireContext(), "You are at Wrong place", Toast.LENGTH_SHORT).show()
//        } else {
//        Log.i("sebastian rList1",rList.toString()+riddleNumber)
//        updateDescriptionBox()
//        }
//    }
//
////    private fun getRiddlesList() {
////        viewModel.getRiddles(eid)
////        Log.i("seb contest","eid${eid}")
////        viewModel.get().observe(viewLifecycleOwner, Observer {
////            rList = it!!
////            onCreateGetRiddleNumber()
////            updateDescriptionBox()
////            Log.i("sebastian rList2",rList.toString())
////        })
////    }
//
//    private fun onLaunchTimeRiddleNumber() {
//        viewModel.getSubmissionDetails(eid) {
//            riddleNumber = it!!
//        }
//
//    }
//    private fun onCreateGetRiddleNumber() {
//        viewModel.getRiddleNumberNumberFirst(eid){
//         riddleNumber = it!!
//        }
//    }
//
//    private fun setupViewModel() {
//        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
//    }
//
//    private fun setupScanner() {
//        val integrator = IntentIntegrator.forSupportFragment(this)
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//        integrator.setPrompt("scan")
//        integrator.setCameraId(0)
//        integrator.setOrientationLocked(true)
//        integrator.setBeepEnabled(true)
//        integrator.setBarcodeImageEnabled(false)
//        integrator.initiateScan()
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.w("sebastian scanResult","not return")
//        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (scanResult != null) {
//            Log.w("sebastian scanResult","not return")
//
//            if (scanResult.contents == null) {
//                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.w("sebastian scanResult",scanResult.contents.toString())
//                binding.etCode.setText(scanResult.contents.toString())
//            }
//        } else {
//            Log.w("sebastian scanResult","not return")
//
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//
//
//    override fun shouldShowRationaleInfo() {
//        permissionHelper.launchPermissionDialog(Manifest.permission.CAMERA)
//    }
//
//    override fun isPermissionGranted(isGranted: Boolean) {
//        Log.w("sebastian scanResult","fun is permission granted")
//
//        if(isGranted){
//            Log.w("sebastian scanResult","granted")
//            setupScanner()
//        }
//    }
//    private fun updateDescriptionBox(){
//        if (riddleNumber == -1) {
//            binding.riddleDiscription.text = "Not Found"
//        } else if (riddleNumber >= rList.size) {
//            val action = ContestFragmentDirections.actionContestFragmentToEventFragment(eid.toLong())
//            findNavController().navigate(action)
//            Toast.makeText(requireContext(), "done ", Toast.LENGTH_SHORT).show()
//        } else {
//                onLaunchTimeRiddleNumber()
//            Log.w("riddle${riddleNumber}",rList[riddleNumber].question)
//            binding.riddleDiscription.text=rList[riddleNumber].question
//            riddleNumber++
//        }
//    }

}