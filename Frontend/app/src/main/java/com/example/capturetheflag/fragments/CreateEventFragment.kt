package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.capturetheflag.ui.CreateEventViewModel
import com.example.capturetheflag.R
import com.example.capturetheflag.databinding.FragmentCreateEventBinding
import com.example.capturetheflag.util.DecodeBase64ImageTask
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText

class CreateEventFragment : Fragment() {
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



    //    private late init var decodeBase64ImageTask: DecodeBase64ImageTask

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
        etTitle=binding.etTitle
        etDescription=binding.etDescription
        etLocation=binding.etLocation
        etOrganisation=binding.etLocation
        etPrizes=binding.etPrizes
        etStartDate=binding.etStartDate
        etEndDate=binding.etEndDate
        etStartTime=binding.etStartTime
        etEndTime=binding.etEndTime

        etStartDate.setOnClickListener {
            val datePicker =MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(childFragmentManager,"Select Start Date")
            datePicker.addOnPositiveButtonClickListener {
                etStartDate.setText(datePicker.headerText)
            }

        }

    }

}