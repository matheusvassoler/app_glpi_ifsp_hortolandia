package com.glpi.ifsp.hortolandia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.glpi.ifsp.hortolandia.databinding.TicketItemBinding
import com.glpi.ifsp.hortolandia.ui.model.TicketUI

class TicketAdapter(
    private val onTicketClick: (ticket: TicketUI?) -> Unit
) : PagingDataAdapter<TicketUI, TicketAdapter.TicketViewHolder>(TicketComparator) {

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = TicketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding, onTicketClick)
    }

    class TicketViewHolder(
        private val itemBinding: TicketItemBinding,
        private val onTicketClick: (ticket: TicketUI?) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val title: TextView = itemBinding.ticketItemTitle
        private val description: TextView = itemBinding.ticketItemDescription
        private val updateDate: TextView = itemBinding.ticketItemUpdateDateValue
        private val status: TextView = itemBinding.ticketItemStatusValue

        fun bind(ticket: TicketUI?) {
            title.text = ticket?.title
            description.text = ticket?.description
            updateDate.text = ticket?.updateDate
            status.text = ticket?.status

            itemBinding.ticketItem.setOnClickListener {
                onTicketClick(ticket)
            }
        }
    }

    object TicketComparator : DiffUtil.ItemCallback<TicketUI>() {
        override fun areItemsTheSame(oldItem: TicketUI, newItem: TicketUI): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TicketUI, newItem: TicketUI): Boolean =
            oldItem == newItem
    }
}
