package com.example.capturetheflag.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.capturetheflag.R
import com.example.capturetheflag.activities.HomeActivity
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.databinding.FragmentLoginBinding
import com.example.capturetheflag.models.LoginReponse
import com.example.capturetheflag.models.UserLoginDetails
import com.example.capturetheflag.session.Session
import com.example.capturetheflag.ui.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref: Session
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerTextView: TextView
    private lateinit var loginButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.actionBar?.hide()
        _binding = FragmentLoginBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = Session((requireActivity()))
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword
        registerTextView = binding.tvRegisternow
        loginButton = binding.btnLogin

        if (sharedPref.isLogin()){
            moveToHome()
        }
        loginButton.setOnClickListener {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()){
                toastMessage("Invalid Email Address")
            }
            else if(passwordEditText.text.toString().isEmpty()){
                toastMessage("Password Field is blank")
            }
            else {
                showProgressBar()
                actionLoginButton()
            }
        }
        registerTextView.setOnClickListener {
            actionRegisterNow()
        }
    }


    private fun toastMessage(s: String) {
    Toast.makeText(requireActivity(),s,Toast.LENGTH_SHORT).show();
    }

    private fun actionRegisterNow() {
        Navigation.findNavController(requireView()).navigate(R.id.action_login_to_registerFragment)
    }

    private fun actionLoginButton() {
        val loginDetails = UserLoginDetails(emailEditText.text.toString(), passwordEditText.text.toString())
        lifecycleScope.launch {
            RetrofitInstances.service.login(loginDetails).enqueue(object : retrofit2.Callback<LoginReponse> {
                override fun onResponse(call: Call<LoginReponse>, response: Response<LoginReponse>) {
                    Log.w("sebestian",response.toString())
                    if (response.isSuccessful){
                        val receivedata = response.body()
                        if (receivedata != null  ) {
                            if(receivedata.success == true){
                                toastMessage("Login successfully")
                                sharedPref.logOut()
                                Log.d("CTF Login1",receivedata.userDetails.toString())
                                Log.d("CTF Login2",receivedata.userDetails.email.toString())
                                receivedata.userDetails.apply {
                                    sharedPref.createSession(
                                        name = "${this.FirstName} ${this.LastName}",
                                        id = this.user_id,
                                        enrollmentID = this.enroll_id,
                                        college = this.CollegeName,
                                        mobile = this.MobileNo,
                                        token = "token_string",
                                        email = this.email
                                    )
                                }
                                moveToHome()
                            } else {
                                hideProgressBar()
                                toastMessage(receivedata.message);
                            }
                        }
                    }
                    else{
                        hideProgressBar()
                        toastMessage("unknown error")
                    }
                }

                override fun onFailure(call: Call<LoginReponse>, t: Throwable) {
                    Log.w("sebestion",t)
                    toastMessage("Unknown Error!")
                }

            })
        }


    }
    private fun moveToHome(){
        hideProgressBar()
        val intent = Intent(requireContext(),HomeActivity::class.java)
        startActivity(intent)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }
    private fun showProgressBar(){
        binding.progress.visibility=View.VISIBLE
    }
    private fun hideProgressBar(){
        binding.progress.visibility=View.INVISIBLE
    }
}