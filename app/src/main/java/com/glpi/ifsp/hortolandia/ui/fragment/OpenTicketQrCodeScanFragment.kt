package com.glpi.ifsp.hortolandia.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketQrCodeScanBinding

class OpenTicketQrCodeScanFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var grantedAccessPermissionCamera = false
    private var _binding: FragmentOpenTicketQrCodeScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultLauncher.launch(Manifest.permission.CAMERA)
        grantedAccessPermissionCamera = checkAccessPermissionCamera()
    }

    override fun onResume() {
        super.onResume()

        if (grantedAccessPermissionCamera) {
            codeScanner.startPreview()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketQrCodeScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentOpenTicketQrCodeScanToolbar.toolbarTitle.text = TOOLBAR_TITLE
        codeScanner = CodeScanner(requireActivity(), binding.scannerView)
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            grantedAccessPermissionCamera = isGranted
        }

    private fun checkAccessPermissionCamera() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onPause() {
        super.onPause()
        if (grantedAccessPermissionCamera) {
            codeScanner.releaseResources()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TOOLBAR_TITLE = "Escanear QR Code"
    }
}
