package com.glpi.ifsp.hortolandia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketQrCodeScanErrorBinding
import com.glpi.ifsp.hortolandia.ui.activity.HomeActivity

class OpenTicketQrCodeScanErrorFragment : Fragment() {

    private var _binding: FragmentOpenTicketQrCodeScanErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketQrCodeScanErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        setupTryAgainButtonListener()
        setupBackToHomeButtonListener()
    }

    private fun setupTryAgainButtonListener() {
        binding.fragmentOpenTicketQrCodeScanErrorCardView.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                activity?.supportFragmentManager?.popBackStack()
            }?.commit()
        }
    }

    private fun setupBackToHomeButtonListener() {
        binding.fragmentOpenTicketQrCodeScanErrorBackCardView.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
