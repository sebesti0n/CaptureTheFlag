package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.capturetheflag.databinding.FragmentRegisterBinding
import com.example.capturetheflag.models.User
import com.example.capturetheflag.ui.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentRegisterBinding.inflate(inflater, container ,false)
        return binding.root
    }

    private fun checkCredentials(): Boolean {
    if(binding.etCnfpassword.text.toString().isEmpty()
        ||binding.etCollege.text.toString().isEmpty()
        ||binding.etEmail.text.toString().isEmpty()
        ||binding.etFirstname.text.toString().isEmpty()
        ||binding.etMobileNo.text.toString().isEmpty()||
        binding.etPassword.text.toString().isEmpty())return true
        return binding.etPassword.text.toString() != binding.etCnfpassword.text.toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.btnLogin.setOnClickListener {
            if(checkCredentials()){
                showToastMessage("fill all details")
            }else {
                val newUser = User(binding.etCollege.text.toString(),
                    binding.etEmail.text.toString(),binding.etFirstname.text.toString(),
                    binding.etLastname.text.toString(),
                    binding.etMobileNo.text.toString(),
                    binding.etCnfpassword.text.toString(),
                    binding.etPassword.text.toString())
                viewModel.register(newUser)
                viewModel.get()?.observe(requireActivity(), Observer {
                    if(it.success){
                        showToastMessage(it.user.password.toString())
                    }else{
                        showToastMessage(it.message)
                    }
                })
            }
        }
    }

    private fun showToastMessage(msg: String) {
    Toast.makeText(requireActivity(),msg,Toast.LENGTH_SHORT).show()
    }

}