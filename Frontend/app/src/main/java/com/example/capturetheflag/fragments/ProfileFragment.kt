package com.example.capturetheflag.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capturetheflag.activities.HomeActivity
import com.example.capturetheflag.activities.MainActivity
import com.example.capturetheflag.databinding.FragmentProfileBinding
import com.example.capturetheflag.session.Session

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
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
        updateUI()
        binding.logoutBtn.setOnClickListener {
            session.logOut()
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        (activity as HomeActivity).startActivity(intent)
        (activity as HomeActivity).finish()
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