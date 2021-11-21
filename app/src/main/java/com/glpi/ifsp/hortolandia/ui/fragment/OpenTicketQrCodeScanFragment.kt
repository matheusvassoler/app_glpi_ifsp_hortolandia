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
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketQrCodeScanBinding
import com.google.zxing.BarcodeFormat

class OpenTicketQrCodeScanFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var grantedAccessPermissionCamera = false
    private var _binding: FragmentOpenTicketQrCodeScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onResume() {
        super.onResume()

        grantedAccessPermissionCamera = checkAccessPermissionCamera()
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

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.activity_open_ticket_container, OpenTicketFormFragment.newInstance(it.text))
                addToBackStack(null)
            }?.commit()
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.activity_open_ticket_container, OpenTicketCameraPermissionFragment())
                    addToBackStack(null)
                }?.commit()
            }
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
