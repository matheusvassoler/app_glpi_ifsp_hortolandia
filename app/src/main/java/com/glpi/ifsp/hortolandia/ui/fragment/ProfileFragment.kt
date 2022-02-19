package com.glpi.ifsp.hortolandia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.FragmentProfileBinding
import com.glpi.ifsp.hortolandia.ui.activity.LoginActivity
import com.glpi.ifsp.hortolandia.ui.event.LogoutEvent
import com.glpi.ifsp.hortolandia.ui.state.LogoutState
import com.glpi.ifsp.hortolandia.ui.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitButtonListener()
        setObservers()
    }

    private fun setObservers() {
        setEventObserver()
        setStateObserver()
    }

    private fun setEventObserver() {
        profileViewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LogoutEvent.GoToLogin -> goToLogin()
                is LogoutEvent.ShowInternalError -> showSnackBar(getString(R.string.internal_error))
                is LogoutEvent.ShowLogoutError -> showSnackBar(getString(R.string.profile_logout_error))
            }
        })
    }

    private fun setStateObserver() {
        profileViewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is LogoutState.ShowLoading -> {
                    configureLoginButton(false, View.VISIBLE, View.GONE)
                }
                is LogoutState.HideLoading -> {
                    configureLoginButton(true, View.GONE, View.VISIBLE)
                }
            }
        })
    }

    private fun exitButtonListener() {
        binding.fragmentProfileLogoutButton.setOnClickListener {
            profileViewModel.onLogoutClick()
        }
    }

    private fun goToLogin() {
        startActivity(LoginActivity.newInstance(requireContext()))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun configureLoginButton(isClickable: Boolean, progressbarVisibility: Int, buttonTextVisibility: Int) {
        binding.fragmentProfileLogoutButton.isClickable = isClickable
        binding.fragmentLogoutProgressBarButton.visibility = progressbarVisibility
        binding.fragmentProfileLogoutButtonText.visibility = buttonTextVisibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
