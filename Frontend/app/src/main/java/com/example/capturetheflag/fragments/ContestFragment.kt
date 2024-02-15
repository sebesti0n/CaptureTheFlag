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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capturetheflag.util.TimeLineAdapter
import com.example.capturetheflag.databinding.FragmentContestBinding
import com.example.capturetheflag.helper.PermissionHelper
import com.example.capturetheflag.models.QuestionModel
import com.example.capturetheflag.room.CtfDatabase
import com.example.capturetheflag.ui.ContestViewModel
import com.example.capturetheflag.util.PermissionListener
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch

class ContestFragment : Fragment(),PermissionListener{
    private var _binding:FragmentContestBinding?=null
    private val binding get() = _binding!!
    private val args:ContestFragmentArgs by navArgs()
    private lateinit var roomDB:CtfDatabase
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
        setupRoomDatabase()
    }

    private fun setupRoomDatabase() {
        roomDB = CtfDatabase.getDatabase(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
           val count = roomDB.CtfTeamStateDao().isTeamRegistered()
        }
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
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


}
