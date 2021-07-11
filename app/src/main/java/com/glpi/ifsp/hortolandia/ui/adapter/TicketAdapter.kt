package com.glpi.ifsp.hortolandia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.databinding.TicketItemBinding

class TicketAdapter : PagingDataAdapter<Ticket, TicketAdapter.TicketViewHolder>(TicketComparator) {

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = TicketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    inner class TicketViewHolder(itemBinding: TicketItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private val title: TextView = itemBinding.ticketItemTitle
        private val description: TextView = itemBinding.ticketItemDescription
        private val updateDate: TextView = itemBinding.ticketItemUpdateDateValue
        private val status: TextView = itemBinding.ticketItemStatusValue

        fun bind(ticket: Ticket?) {
            title.text = ticket?.title
            description.text = ticket?.content
            updateDate.text = ticket?.updateDate
            status.text = ticket?.status.toString()
        }
    }

    object TicketComparator : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean =
            oldItem == newItem
    }
}
