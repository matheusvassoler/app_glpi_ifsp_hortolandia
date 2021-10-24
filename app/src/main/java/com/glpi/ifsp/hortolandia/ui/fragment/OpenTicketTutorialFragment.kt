package com.glpi.ifsp.hortolandia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketTutorialBinding

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
}
