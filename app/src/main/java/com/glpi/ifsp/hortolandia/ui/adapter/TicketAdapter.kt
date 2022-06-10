package com.glpi.ifsp.hortolandia.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.TicketItemBinding
import com.glpi.ifsp.hortolandia.ui.model.TicketUI

class TicketAdapter(
    private val onTicketClick: (ticket: TicketUI?) -> Unit,
    private val context: Context
) : PagingDataAdapter<TicketUI, TicketAdapter.TicketViewHolder>(TicketComparator) {

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = TicketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding, onTicketClick, context)
    }

    class TicketViewHolder(
        private val itemBinding: TicketItemBinding,
        private val onTicketClick: (ticket: TicketUI?) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val title: TextView = itemBinding.ticketItemTitle
        private val description: TextView = itemBinding.ticketItemDescription
        private val updateDate: TextView = itemBinding.ticketItemUpdateDateLabel
        private val statusIndicatorIcon: ImageView = itemBinding.statusIndicator
        private val statusLabel: TextView = itemBinding.ticketItemUpdateStatusLabel

        fun bind(ticket: TicketUI?) {
            title.text = ticket?.title
            description.text = ticket?.description
            updateDate.text =
                context.getString(R.string.ticket_update_date_label, ticket?.updateDate)
            statusLabel.text = ticket?.status

            setTicketStatusIndicator(ticket)

            itemBinding.ticketItem.setOnClickListener {
                onTicketClick(ticket)
            }
        }

        private fun setTicketStatusIndicator(ticket: TicketUI?) {
            when (ticket?.intStatus) {
                STATUS_NEW -> setStatusIndicatorColor(R.drawable.gray_circle_shape)
                STATUS_PROCESSING -> setStatusIndicatorColor(R.drawable.yellow_circle_shape)
                STATUS_PENDING -> setStatusIndicatorColor(R.drawable.orange_circle_shape)
                STATUS_SOLVED -> setStatusIndicatorColor(R.drawable.blue_circle_shape)
                STATUS_CLOSED -> setStatusIndicatorColor(R.drawable.green_circle_shape)
            }
        }

        private fun setStatusIndicatorColor(drawableId: Int) {
            statusIndicatorIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    drawableId
                )
            )
        }
    }

    object TicketComparator : DiffUtil.ItemCallback<TicketUI>() {
        override fun areItemsTheSame(oldItem: TicketUI, newItem: TicketUI): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TicketUI, newItem: TicketUI): Boolean =
            oldItem == newItem
    }

    companion object {
        private const val STATUS_NEW = 1
        private const val STATUS_PROCESSING = 2
        private const val STATUS_PENDING = 3
        private const val STATUS_SOLVED = 4
        private const val STATUS_CLOSED = 5
    }
}
