package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityTicketDetailsBinding
import com.glpi.ifsp.hortolandia.infrastructure.Constant.ExtrasName.TICKET_UI
import com.glpi.ifsp.hortolandia.ui.model.TicketUI

class TicketDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        checkIfIntentContainsTicketUI()
    }

    private fun setToolbar() {
        binding.activityTicketDetailsToolbar.toolbarTitle.text = getString(R.string.ticket_details_toolbar_title)
        binding.activityTicketDetailsToolbar.toolbarLeftIcon.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.back_button_icon, null)
        )
        binding.activityTicketDetailsToolbar.toolbarLeftIcon.setOnClickListener {
            finish()
        }
    }

    private fun checkIfIntentContainsTicketUI() {
        if (intent.hasExtra(TICKET_UI)) {
            val ticketUi = intent.getParcelableExtra<TicketUI>(TICKET_UI)
            ticketUi?.let {
                bindViewWithTicketUI(it)
            }
        } else {
            startActivity(Intent(this, ErrorActivity::class.java))
            finish()
        }
    }

    private fun bindViewWithTicketUI(ticketUI: TicketUI) {
        binding.activityTicketDetailsIdValue.text = ticketUI.id
        binding.activityTicketDetailsTitleValue.text = ticketUI.title
        binding.activityTicketDetailsDescriptionValue.text = ticketUI.description
        binding.activityTicketDetailsOpeningDateValue.text =
            getString(R.string.ticket_details_date_value, ticketUI.openingDate, ticketUI.openingHour)
        binding.activityTicketDetailsUpdateDateValue.text =
            getString(R.string.ticket_details_date_value, ticketUI.updateDate, ticketUI.updateHour)
        binding.activityTicketDetailsStatusValue.text = ticketUI.status
        binding.activityTicketDetailsProgressBarStatus.progress = ticketUI.percentageStatusProgress
        setProgressBarAccordingTicketStatus(ticketUI)
    }

    private fun setProgressBarAccordingTicketStatus(ticketUI: TicketUI) {
        when (ticketUI.percentageStatusProgress) {
            ZERO_PERCENT -> setProgressBarStatus(R.drawable.custom_progress_bar)
            TWENTY_FIVE_PERCENT -> setProgressBarStatus(R.drawable.custom_progress_bar2)
            FIFTY_PERCENT -> setProgressBarStatus(R.drawable.custom_progress_bar3)
            SEVENTY_FIVE_PERCENT -> setProgressBarStatus(R.drawable.custom_progress_bar4)
            ONE_HUNDRED_PERCENT -> setProgressBarStatus(R.drawable.custom_progress_bar5)
        }
    }

    private fun setProgressBarStatus(drawableId: Int) {
        binding.activityTicketDetailsProgressBarStatus.progressDrawable =
            ContextCompat.getDrawable(this, drawableId)
    }

    companion object {
        private const val ZERO_PERCENT = 0
        private const val TWENTY_FIVE_PERCENT = 25
        private const val FIFTY_PERCENT = 50
        private const val SEVENTY_FIVE_PERCENT = 75
        private const val ONE_HUNDRED_PERCENT = 100
    }
}
