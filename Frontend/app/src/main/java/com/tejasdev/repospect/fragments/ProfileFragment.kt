package com.tejasdev.repospect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tejasdev.repospect.activities.HomeActivity
import com.tejasdev.repospect.activities.MainActivity
import com.tejasdev.repospect.databinding.FragmentProfileBinding
import com.tejasdev.repospect.session.Session
import com.tejasdev.repospect.ui.ContestViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContestViewModel
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session = Session.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[ContestViewModel::class.java]
        updateUI()
        binding.logoutBtn.setOnClickListener {
            viewModel.logOut()
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
    }

    private fun updateUI() {
        binding.apply {
            userNameTv.text = session.getUserName()
            userCollegeTv.text = session.getCollege()
            usernameTv.text = session.getUserName()
            emailNameTv.text = session.getEmail()
            enrollmentTv.text = session.getEnrollmentID()
            mobileTv.text = session.getMobile()

            //call to update user image"
        }
    }
}