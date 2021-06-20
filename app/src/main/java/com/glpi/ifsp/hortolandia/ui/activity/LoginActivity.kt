package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityLoginBinding
import com.glpi.ifsp.hortolandia.ui.event.LoginEvent
import com.glpi.ifsp.hortolandia.ui.state.LoginState
import com.glpi.ifsp.hortolandia.ui.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLoginButtonListener()
        setupObservers()
    }

    private fun setupLoginButtonListener() {
        binding.activityLoginButton.setOnClickListener {
            val username = binding.activityLoginUsernameInput.editText?.text.toString()
            val password = binding.activityLoginPasswordInput.editText?.text.toString()
            loginViewModel.onLoginClick(username, password)
        }
    }

    private fun setupObservers() {
        setupStateObserver()
        setupEventObserver()
    }

    private fun setupEventObserver() {
        loginViewModel.event.observe(this, Observer {
            when (it) {
                is LoginEvent.OpenHome -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is LoginEvent.ShowBadCredentialError -> {
                    showSnackBar(getString(R.string.login_bad_credentials_error_message))
                }
                is LoginEvent.ShowConnectionError -> {
                    showSnackBar(getString(R.string.login_error_message))
                }
            }
        })
    }

    private fun setupStateObserver() {
        loginViewModel.state.observe(this, Observer {
            when (it) {
                is LoginState.ShowLoading -> {
                    configureLoginButton(false, View.VISIBLE, View.GONE)
                }
                is LoginState.HideLoading -> {
                    configureLoginButton(true, View.GONE, View.VISIBLE)
                }
                is LoginState.ValidateField -> {
                    checkIfShowErrorMessageForUsernameAndPasswordField(it.isUsernameEmpty, it.isPasswordEmpty)
                }
            }
        })
    }

    private fun checkIfShowErrorMessageForUsernameAndPasswordField(isUsernameEmpty: Boolean, isPasswordEmpty: Boolean) {
        checkIfShowErrorMessageForField(
            isUsernameEmpty,
            binding.activityLoginUsernameInput,
            getString(R.string.empty_username_field_error_message)
        )
        checkIfShowErrorMessageForField(
            isPasswordEmpty,
            binding.activityLoginPasswordInput,
            getString(R.string.empty_password_field_error_message)
        )
    }

    private fun checkIfShowErrorMessageForField(isFieldEmpty: Boolean, field: TextInputLayout, message: String) {
        if (isFieldEmpty) {
            controlErrorDisplayForEmptyField(field, message)
        } else {
            controlErrorDisplayForEmptyField(field)
        }
    }

    private fun configureLoginButton(isClickable: Boolean, progressbarVisibility: Int, buttonTextVisibility: Int) {
        binding.activityLoginButton.isClickable = isClickable
        binding.activityLoginProgressBarButton.visibility = progressbarVisibility
        binding.activityLoginButtonText.visibility = buttonTextVisibility
    }

    private fun controlErrorDisplayForEmptyField(textInputLayout: TextInputLayout, errorMessage: String? = null) {
        textInputLayout.error = errorMessage
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
