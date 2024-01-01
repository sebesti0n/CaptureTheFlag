package com.example.capturetheflag.helper

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capturetheflag.util.PermissionListener

class PermissionHelper(context:Fragment,listner: PermissionListener) {
    private var context:Fragment?=null
    private var listner:PermissionListener?=null
    init {
        this.context=context
        this.listner=listner
    }

    private val requestPermissionLauncher = context.registerForActivityResult(

        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            listner.isPermissionGranted(true)
        } else {
            Log.w("Permission:", "NotGranted")
        }
    }
    private val requestMultiplePermissionsLauncher = context.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach {
            listner.isPermissionGranted(true)
        }
    }
    fun checkPermissions(mPermission:String){
        when {
            context?.requireContext()?.let{
                ContextCompat.checkSelfPermission( it, mPermission)
            } == PackageManager.PERMISSION_GRANTED->{
                listner?.isPermissionGranted(true)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                context?.requireContext() as Activity,
                mPermission
            ) -> {
                listner?.isPermissionGranted(false)
                listner?.shouldShowRationaleInfo()
            }
            else ->{

            }
        }
    }
    private var isDenied : Boolean  = false
    fun checkForMultiplePermissions(manifestPermissions: Array<String>) {
        for (permission in manifestPermissions) {
            context?.requireContext()?.let {
                if (ContextCompat.checkSelfPermission(
                        it,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    listner?.isPermissionGranted(true)
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context?.requireContext() as Activity,
                        permission
                    )
                ) {
                    isDenied = true
                } else {
                    requestMultiplePermissionsLauncher.launch(manifestPermissions)
                }
            }
        }
        if(isDenied){
            listner?.isPermissionGranted(false)
            listner?.shouldShowRationaleInfo()
        }
    }
    fun launchPermissionDialogForMultiplePermissions(manifestPermissions: Array<String>){
        requestMultiplePermissionsLauncher.launch(manifestPermissions)
    }
    fun launchPermissionDialog(mPermission: String){
        requestPermissionLauncher.launch(
            mPermission
        )
    }
}
