package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityLoginBinding
import com.glpi.ifsp.hortolandia.ui.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    private fun setupObservers() {
        setupObserverUsernameField()
        setupObserverForPasswordField()
    }

    private fun setupObserverForPasswordField() {
        loginViewModel.isPasswordEmpty.observe(this, {
            if (it) {
                controlErrorDisplayForEmptyField(
                    binding.activityLoginPasswordInput,
                    getString(R.string.empty_password_field_error_message)
                )
            } else {
                controlErrorDisplayForEmptyField(binding.activityLoginPasswordInput)
            }
        })
    }

    private fun setupObserverUsernameField() {
        loginViewModel.isUsernameEmpty.observe(this, {
            if (it) {
                controlErrorDisplayForEmptyField(
                    binding.activityLoginUsernameInput,
                    getString(R.string.empty_username_field_error_message)
                )
            } else {
                controlErrorDisplayForEmptyField(binding.activityLoginUsernameInput)
            }
        })
    }

    private fun controlErrorDisplayForEmptyField(textInputLayout: TextInputLayout, errorMessage: String? = null) {
        textInputLayout.error = errorMessage
    }
}
