package com.example.capturetheflag.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capturetheflag.R
import com.example.capturetheflag.ui.HistoryHuntViewModel

class HistoryHuntFragment:Fragment() {
    private lateinit var viewModel: HistoryHuntViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_hunt, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryHuntViewModel::class.java)
        // TODO: Use the ViewModel
    }
}