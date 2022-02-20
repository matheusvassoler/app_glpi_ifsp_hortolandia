package com.glpi.ifsp.hortolandia.gateway

import android.content.Context
import com.glpi.ifsp.hortolandia.R

class DefaultLabelGateway(private val context: Context) : LabelGateway {

    override fun getLabelForStatusNew(): String {
        return context.getString(R.string.ticket_details_status_new_label)
    }

    override fun getLabelForStatusProcessing(): String {
        return context.getString(R.string.ticket_details_status_processing_label)
    }

    override fun getLabelForStatusPending(): String {
        return context.getString(R.string.ticket_details_status_pending_label)
    }

    override fun getLabelForStatusSolved(): String {
        return context.getString(R.string.ticket_details_status_solved_label)
    }

    override fun getLabelForStatusClosed(): String {
        return context.getString(R.string.ticket_details_status_closed_label)
    }
}
