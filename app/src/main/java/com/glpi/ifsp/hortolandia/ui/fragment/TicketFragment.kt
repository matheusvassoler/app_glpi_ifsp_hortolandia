package com.glpi.ifsp.hortolandia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.glpi.ifsp.hortolandia.databinding.FragmentTicketBinding
import com.glpi.ifsp.hortolandia.ui.adapter.TicketAdapter
import com.glpi.ifsp.hortolandia.ui.viewmodel.TicketViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class TicketFragment : Fragment() {

    private var _binding: FragmentTicketBinding? = null
    private val binding get() = _binding!!

    private val ticketViewModel: TicketViewModel by viewModel()
    private lateinit var ticketAdapter: TicketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketBinding.inflate(inflater, container, false)
        ticketViewModel.onStart()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ticketAdapter = TicketAdapter()

        setRecyclerView()
        setPagingDataToAdapter()
        setListenerToSwipeRefresh()
    }

    private fun setRecyclerView() {
        binding.fragmentTicketList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ticketAdapter
        }
    }

    private fun setPagingDataToAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            ticketViewModel.ticketFlow.collectLatest { ticketPagingData ->
                ticketAdapter.submitData(ticketPagingData)
            }
        }
    }

    private fun setListenerToSwipeRefresh() {
        binding.fragmentTicketSwipeRefresh.setOnRefreshListener {
            ticketAdapter.refresh()
            binding.fragmentTicketSwipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
