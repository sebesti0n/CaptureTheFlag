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
import androidx.navigation.Navigation
import com.example.capturetheflag.R
import com.example.capturetheflag.activities.HomeActivity
import com.example.capturetheflag.databinding.FragmentRegisterBinding
import com.example.capturetheflag.models.User
import com.example.capturetheflag.sharedprefrences.userPreferences
import com.example.capturetheflag.ui.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref:userPreferences
    private lateinit var viewModel: RegisterViewModel
    private lateinit var firstName:String
    private lateinit var lastName:String
    private lateinit var email:String
    private lateinit var mobileNo:String
    private lateinit var collegeName:String
    private lateinit var password:String
    private lateinit var cnfPassword:String



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
    if(cnfPassword.isEmpty() || collegeName.isEmpty() || email.isEmpty() || firstName.isEmpty() || mobileNo.isEmpty() || password.isEmpty())
        return true
        return password != cnfPassword
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = userPreferences(requireActivity())
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]




        binding.btnLogin.setOnClickListener {

            email = binding.etEmail.text.toString()
            firstName = binding.etFirstname.text.toString()
            lastName =binding.etLastname.text.toString()
            collegeName = binding.etCollege.text.toString()
            mobileNo = binding.etMobileNo.text.toString()
            password = binding.etPassword.text.toString()
            cnfPassword = binding.etCnfpassword.text.toString()

            if(checkCredentials()){
                showToastMessage("fill all details")
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToastMessage("Enter Correct Email")
            }else
             {
                val newUser = User(collegeName,email,firstName,lastName,mobileNo,cnfPassword,password)
                viewModel.register(newUser)
                viewModel.get()?.observe(requireActivity(), Observer {
                    if(it.success){
                       sharedPref.createSession(it.user.user_id,it.user.Email,true,it.message)
                        moveToHome()

                    }else{
                        showToastMessage(it.message)
                    }
                })
            }
        }
    }

    private fun moveToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)    }

    private fun showToastMessage(msg: String) {
    Toast.makeText(requireActivity(),msg,Toast.LENGTH_SHORT).show()
    }

}