package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityTicketDetailsBinding

class TicketDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
    }

    private fun setToolbar() {
        binding.activityTicketDetailsToolbar.toolbarTitle.text = getString(R.string.ticket_details_toolbar_title)
        binding.activityTicketDetailsToolbar.toolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.back_button_icon, null)
        binding.activityTicketDetailsToolbar.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.activityTicketDetailsToolbar.toolbar.inflateMenu(R.menu.ticket_details_toolbar_menu)
    }
}
