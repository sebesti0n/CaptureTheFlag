package com.example.capturetheflag.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.capturetheflag.R
import com.example.capturetheflag.apiServices.RetrofitInstances
import com.example.capturetheflag.databinding.FragmentLoginBinding
import com.example.capturetheflag.models.LoginReponse
import com.example.capturetheflag.models.UserLoginDetails
import com.example.capturetheflag.sharedprefrences.userPreferences
import com.example.capturetheflag.ui.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Pattern
import javax.security.auth.callback.Callback

class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref:userPreferences
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerTextView: TextView
    private lateinit var loginButton: AppCompatButton
    private lateinit var googleButton: ImageView

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
        sharedPref = userPreferences((requireActivity()))
        emailEditText = binding.etEmail
        passwordEditText = binding.etPassword
        registerTextView = binding.tvRegisternow
        loginButton = binding.btnLogin
        googleButton = binding.btnGoogle

        if (sharedPref.isLogin()){
            moveToHome()
        }

        googleButton.setOnClickListener{
            googleRegistration()
        }
        loginButton.setOnClickListener {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()){
                toastMessage("Invalid Email Address")
            }
            else if(passwordEditText.text.toString().isEmpty()){
                toastMessage("Password Field is blank")
            }
            else {
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
                                sharedPref.createSession(receivedata.userDetails.user_id,receivedata.userDetails.Email,true,"token")
                                moveToHome()
                            } else {
                                toastMessage(receivedata.message);
                            }
                        }
                    }
                    else{
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

    private fun googleRegistration() {
        TODO("Not yet implemented")
    }
    private fun moveToHome(){
        Navigation.findNavController(requireView()).navigate(R.id.action_login_to_homefragment)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        // TODO: Use the ViewModel
    }

}