package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capturetheflag.R
import com.example.capturetheflag.ui.RegisterHuntViewModel

class RegisterHuntFragment : Fragment() {



    private lateinit var viewModel: RegisterHuntViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_hunt, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterHuntViewModel::class.java)
        // TODO: Use the ViewModel
    }

}