package com.example.capturetheflag.fragments

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capturetheflag.R
import com.example.capturetheflag.activities.HomeActivity
import com.example.capturetheflag.databinding.FragmentRegisterBinding
import com.example.capturetheflag.helper.NetworkHelper
import com.example.capturetheflag.models.User
import com.example.capturetheflag.session.Session
import com.example.capturetheflag.ui.RegisterViewModel
import com.example.capturetheflag.util.Resource
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: Session
    private lateinit var viewModel: RegisterViewModel
    private lateinit var firstName:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var mobileNo:String
    private lateinit var collegeName:String
    private lateinit var password:String
    private lateinit var cnfPassword:String
    private lateinit var enrollmentID:String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.actionBar?.hide()
        _binding=FragmentRegisterBinding.inflate(inflater, container ,false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkCredentials(): Boolean {
    if(cnfPassword.isEmpty() || collegeName.isEmpty() || email.isEmpty() || firstName.isEmpty() || mobileNo.isEmpty() || password.isEmpty() || enrollmentID.isEmpty())
        return true
        return password != cnfPassword
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = Session(requireActivity())
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_login)
        }

        binding.btnLogin.setOnClickListener {
            email = binding.etEmail.text.toString()
            firstName = binding.etFirstname.text.toString()
            lastName =binding.etLastname.text.toString()
            collegeName = binding.etCollege.text.toString()
            mobileNo = binding.etMobileNo.text.toString()
            password = binding.etPassword.text.toString()
            cnfPassword = binding.etCnfpassword.text.toString()
            enrollmentID = binding.etEnrollmentId.text.toString()

            if(checkCredentials()){
                showToastMessage("fill all details")
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToastMessage("Enter Correct Email")
            }else
             {if(!NetworkHelper.isInternetAvailable(requireContext()))showToastMessage("No Network Available")
             else {
                 showProgressBar()
                 val newUser = User(
                     collegeName,
                     email,
                     firstName,
                     lastName,
                     mobileNo,
                     cnfPassword,
                     password,
                     enrollmentID
                 )
                 viewModel.register(newUser) { success, message, user ->
                     if (success && user != null) {
                         user.let {
                             sharedPref.createSession(
                                 college = it.CollegeName,
                                 name = "${it.FirstName} ${it.LastName}",
                                 mobile = it.MobileNo,
                                 email = it.email,
                                 enrollmentID = it.enroll_id,
                                 id = it.user_id,
                                 token = "token_String"
                             )
                         }
                         moveToHome()

                     } else {
                         hideProgressBar()
                         Snackbar.make(requireView(), message!!, 2000).show()
                     }
                 }
             }
             }
        }
    }

    private fun moveToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)    }

    private fun showToastMessage(msg: String) {
        Snackbar.make(requireView(),msg,2000).show()
    }

    private fun hideProgressBar(){
        binding.apply {
            progressBar.visibility = View.GONE
            llForm.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar(){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            llForm.visibility = View.GONE
        }
    }

}