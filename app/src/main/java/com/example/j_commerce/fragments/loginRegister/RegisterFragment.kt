package com.example.j_commerce.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.j_commerce.R
import com.example.j_commerce.data.User
import com.example.j_commerce.databinding.FragmentRegisterBinding
import com.example.j_commerce.util.RegisterValidation
import com.example.j_commerce.util.Resource
import com.example.j_commerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegister.setOnClickListener{
                val user = User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailandPassword(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.buttonRegister.startAnimation()
                    }
                    is Resource.Success ->{
                        Log.d("test",it.data.toString())
                        binding.buttonRegister.revertAnimation()
                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        binding.buttonRegister.revertAnimation()
                    }
                    else -> Unit //do nothing
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{ validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmailRegister.apply{
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPasswordRegister.apply{
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }

        }
    }

}