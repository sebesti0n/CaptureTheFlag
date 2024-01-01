package com.example.capturetheflag.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.capturetheflag.databinding.FragmentContestBinding
import com.example.capturetheflag.helper.PermissionHelper
import com.example.capturetheflag.util.PermissionListener
import com.google.zxing.integration.android.IntentIntegrator

class ContestFragment : Fragment(),PermissionListener{
    private var _binding:FragmentContestBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContestViewModel
    private lateinit var permissionHelper: PermissionHelper
//    private lateinit var listener:PermissionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        permissionHelper = PermissionHelper(this,this)
        binding.btnScan.setOnClickListener {
            Log.w("sebastian ","button clicked")
            permissionHelper.checkPermissions(Manifest.permission.CAMERA)
        }
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ContestViewModel::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w("sebastian scanResult","not return")
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {
            Log.w("sebastian scanResult","not return")

            if (scanResult.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Log.w("sebastian scanResult",scanResult.contents.toString())
                binding.etTitle.setText(scanResult.contents.toString())
            }
        } else {
            Log.w("sebastian scanResult","not return")

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

//            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            setupScanner()
        }
    }
}