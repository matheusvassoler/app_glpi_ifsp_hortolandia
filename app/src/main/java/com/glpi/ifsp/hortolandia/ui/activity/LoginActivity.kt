package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.data.resource.Resource
import com.glpi.ifsp.hortolandia.databinding.ActivityLoginBinding
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
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

        setupObservers()
        setupLoginButtonListener()
    }

    private fun setupLoginButtonListener() {
        binding.activityLoginButton.setOnClickListener {
            val username = binding.activityLoginUsernameInput.editText?.text.toString()
            val password = binding.activityLoginPasswordInput.editText?.text.toString()
            loginViewModel.makeLogin(username, password)
        }
    }

    private fun setupObservers() {
        setupObserverUsernameField()
        setupObserverForPasswordField()
        setupObserverForProgressBar()
        setupObserverForLoginRequest()
    }

    private fun setupObserverForLoginRequest() {
        loginViewModel.loginState.observe(this, {
            it.getContentIfNotHandled()?.let { resource ->
                checkIfShowErrorMessageOrMakeLogin(resource)
            }
        })
    }

    private fun setupObserverForProgressBar() {
        loginViewModel.showLoading.observe(this, {
            checkIfShowProgressBarInLoginButton(it)
        })
    }

    private fun setupObserverUsernameField() {
        loginViewModel.isUsernameEmpty.observe(this, {
            checkIfShowErrorMessageForField(
                it,
                binding.activityLoginUsernameInput,
                getString(R.string.empty_username_field_error_message)
            )
        })
    }

    private fun setupObserverForPasswordField() {
        loginViewModel.isPasswordEmpty.observe(this, {
            checkIfShowErrorMessageForField(
                it,
                binding.activityLoginPasswordInput,
                getString(R.string.empty_password_field_error_message)
            )
        })
    }

    private fun checkIfShowErrorMessageForField(isFieldEmpty: Boolean, field: TextInputLayout, message: String) {
        if (isFieldEmpty) {
            controlErrorDisplayForEmptyField(field, message)
        } else {
            controlErrorDisplayForEmptyField(field)
        }
    }

    private fun checkIfShowProgressBarInLoginButton(it: Boolean) {
        if (it) {
            configureLoginButton(false, View.VISIBLE, View.GONE)
        } else {
            configureLoginButton(true, View.GONE, View.VISIBLE)
        }
    }

    private fun configureLoginButton(isClickable: Boolean, progressbarVisibility: Int, buttonTextVisibility: Int) {
        binding.activityLoginButton.isClickable = isClickable
        binding.activityLoginProgressBarButton.visibility = progressbarVisibility
        binding.activityLoginButtonText.visibility = buttonTextVisibility
    }

    private fun checkIfShowErrorMessageOrMakeLogin(resource: Resource<Unit>) {
        when (resource) {
            is Resource.Success -> Toast.makeText(this, "Fez login", Toast.LENGTH_SHORT).show()
            is Resource.Error -> {
                displayLoginErrorMessageAccordingExceptionType(resource)
            }
        }
    }

    private fun displayLoginErrorMessageAccordingExceptionType(resource: Resource<Unit>) {
        if (resource.exception is UnauthorizedLoginException) {
            showSnackBar(getString(R.string.login_bad_credentials_error_message))
        } else {
            showSnackBar(getString(R.string.login_error_message))
        }
    }

    private fun controlErrorDisplayForEmptyField(textInputLayout: TextInputLayout, errorMessage: String? = null) {
        textInputLayout.error = errorMessage
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
