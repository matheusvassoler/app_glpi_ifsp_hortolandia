package com.glpi.ifsp.hortolandia.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glpi.ifsp.hortolandia.databinding.TicketLoadingItemStateBinding

class TicketLoadStateAdapter(
    private val onRetryButtonClickListener: () -> Unit
) : LoadStateAdapter<TicketLoadStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = TicketLoadingItemStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(itemBinding: TicketLoadingItemStateBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val progressBar: ProgressBar = itemBinding.ticketLoadingItemStateProgressBar
        private val retryButton: CardView = itemBinding.ticketLoadingItemStateTryAgainButton

        fun bind(loadState: LoadState) {
            retryButton.visibility = toVisibility(loadState is LoadState.Error)
            progressBar.visibility = toVisibility(loadState is LoadState.Loading)

            retryButton.setOnClickListener {
                onRetryButtonClickListener()
            }
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
