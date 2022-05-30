package com.glpi.ifsp.hortolandia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketTutorialBinding
import com.glpi.ifsp.hortolandia.ui.activity.HomeActivity

class OpenTicketTutorialFragment : Fragment() {

    private var _binding: FragmentOpenTicketTutorialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setListenerForCardView()
        setCallbackForOnBackPressed()
    }

    private fun setListenerForCardView() {
        binding.fragmentOpenTicketTutorialCardView.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.activity_open_ticket_container, OpenTicketQrCodeScanFragment())
                addToBackStack(null)
            }?.commit()
        }
    }

    private fun setCallbackForOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val intent = Intent(requireContext(), HomeActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }
    }

    private fun setToolbar() {
        binding.fragmentOpenTicketTutorialToolbar.toolbarTitle.text =
            getString(R.string.open_ticket_toolbar_title)
        binding.fragmentOpenTicketTutorialToolbar.toolbarLeftIcon.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.back_button_icon, null)
        )

        binding.fragmentOpenTicketTutorialToolbar.toolbarLeftIcon.setOnClickListener {
            goToHomeActivity()
        }
    }

    private fun goToHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
