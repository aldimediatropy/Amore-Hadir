package com.setalis.amorehr.views.auth.login

import android.content.Context
import com.setalis.amorehr.AuthActivity
import com.setalis.amorehr.BuildConfig
import com.setalis.amorehr.base.AmFragment
import com.setalis.amorehr.databinding.FragmentLoginBinding
import com.setalis.amorehr.viewmodels.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */
class LoginFragment: AmFragment<FragmentLoginBinding, AuthActivity>() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun viewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun userInterface(context: Context) {
        super.userInterface(context)
    }

    override fun listener() {
        super.listener()

        if(BuildConfig.DEBUG) {
            binding.editTextEmail.setText("dewa17a@gmail.com")
            binding.editTextPassword.setText("P@ssw0rd.1@4mor3")
        }

        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            authViewModel.login(email, password)
        }else{
            amActivity?.message("Please fill in the email and password")
        }

    }

    override fun observer() {
        super.observer()

        authViewModel.apply {
            amActivity?.loader(true)
            loading.observe(viewLifecycleOwner) { amActivity?.loader(it.getEventIfNotHandled() == true) }
            login.observe(viewLifecycleOwner) {
                it.getEventIfNotHandled()?.let {
                    amActivity?.message(it.user?.email.toString())
                    amActivity?.goToMain()
                }
            }
        }
    }

}