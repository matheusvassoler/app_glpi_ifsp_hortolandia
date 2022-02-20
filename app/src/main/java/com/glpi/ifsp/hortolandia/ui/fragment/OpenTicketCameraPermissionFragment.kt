package com.glpi.ifsp.hortolandia.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketCameraPermissionBinding
import com.glpi.ifsp.hortolandia.ui.activity.HomeActivity

class OpenTicketCameraPermissionFragment : Fragment() {

    private var _binding: FragmentOpenTicketCameraPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        val grantedAccessPermissionCamera = checkAccessPermissionCamera()
        if (grantedAccessPermissionCamera) {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketCameraPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCallbackForOnBackPressed()
        binding.fragmentOpenTicketCameraPermissionCardView.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts(PACKAGE, activity?.packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        binding.fragmentOpenTicketCameraPermissionBackCardView.setOnClickListener {
            goToHome()
        }
    }

    private fun setCallbackForOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            goToHome()
        }
    }

    private fun goToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun checkAccessPermissionCamera() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PACKAGE = "package"
    }
}
