package com.glpi.ifsp.hortolandia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.glpi.ifsp.hortolandia.databinding.FragmentTicketBinding
import com.glpi.ifsp.hortolandia.infrastructure.Constant.ExtrasName.TICKET_UI
import com.glpi.ifsp.hortolandia.ui.activity.ErrorActivity
import com.glpi.ifsp.hortolandia.ui.activity.LoginActivity
import com.glpi.ifsp.hortolandia.ui.activity.TicketDetailsActivity
import com.glpi.ifsp.hortolandia.ui.adapter.TicketAdapter
import com.glpi.ifsp.hortolandia.ui.adapter.TicketLoadStateAdapter
import com.glpi.ifsp.hortolandia.ui.event.TicketEvent
import com.glpi.ifsp.hortolandia.ui.model.TicketUI
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ticketAdapter = TicketAdapter(::onTicketClick, requireContext())

        setEventObserver()
        ticketViewModel.onStart()
        setRecyclerView()
        setPagingDataToAdapter()
        setListeners()
    }

    private fun setRecyclerView() {
        binding.fragmentTicketList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = with(ticketAdapter) {
                setAddLoadStateListener()
                withLoadStateFooter(TicketLoadStateAdapter(::onRetryLoadTicketList))
            }
        }
    }

    private fun TicketAdapter.setAddLoadStateListener() {
        addLoadStateListener { loadState ->
            binding.fragmentTicketSwipeRefresh.isVisible = loadState.refresh is LoadState.NotLoading
            binding.fragmentTicketErrorState.isVisible = loadState.refresh is LoadState.Error
            binding.fragmentTicketInitialLoading.isVisible = loadState.refresh is LoadState.Loading
            checkIfShowEmptyStateOrTicketList(loadState)
        }
    }

    private fun checkIfShowEmptyStateOrTicketList(loadState: CombinedLoadStates) {
        if (loadState.refresh is LoadState.NotLoading && ticketAdapter.itemCount < 1) {
            binding.fragmentTicketSwipeRefresh.isVisible = false
            binding.fragmentTicketEmptyState.isVisible = true
        } else {
            binding.fragmentTicketEmptyState.isVisible = false
        }
    }

    private fun setPagingDataToAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            ticketViewModel.ticketFlow.collectLatest { ticketPagingData ->
                ticketAdapter.submitData(ticketPagingData)
            }
        }
    }

    private fun setListeners() {
        setListenerToSwipeRefresh()
        setListenerToRetryButton()
    }

    private fun setEventObserver() {
        ticketViewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TicketEvent.ShowInternalError -> openErrorActivity()
                is TicketEvent.OpenLoginScreen -> startActivity(LoginActivity.newInstance(requireContext()))
            }
        })
    }

    private fun openErrorActivity() {
        val intent = Intent(requireContext(), ErrorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setListenerToSwipeRefresh() {
        binding.fragmentTicketSwipeRefresh.setOnRefreshListener {
            ticketAdapter.refresh()
            binding.fragmentTicketSwipeRefresh.isRefreshing = false
            binding.fragmentTicketSwipeRefresh.isVisible = false
        }
    }

    private fun setListenerToRetryButton() {
        binding.fragmentTicketErrorTryAgainButton.setOnClickListener {
            ticketAdapter.refresh()
        }
    }

    private fun onRetryLoadTicketList() {
        ticketAdapter.retry()
    }

    private fun onTicketClick(ticketUI: TicketUI?) {
        Intent(requireContext(), TicketDetailsActivity::class.java).apply {
            putExtra(TICKET_UI, ticketUI)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
