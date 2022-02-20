package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.databinding.ActivityFailedRegistrationBinding
import com.glpi.ifsp.hortolandia.ui.event.OpenTicketEvent
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.glpi.ifsp.hortolandia.ui.viewmodel.OpenTicketViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FailedRegistrationActivity : AppCompatActivity() {

    companion object {
        private const val ANSWERS_TO_SAVE_EXTRA = "ANSWERS_TO_SAVE_EXTRA"
        private const val TICKET_TITLE_EXTRA = "TICKET_TITLE_EXTRA"

        fun newInstance(
            context: Context,
            ticketTitle: String,
            answersToSave: HashMap<String, String>
        ): Intent {
            return Intent(context, FailedRegistrationActivity::class.java).apply {
                putExtra(ANSWERS_TO_SAVE_EXTRA, answersToSave)
                putExtra(TICKET_TITLE_EXTRA, ticketTitle)
            }
        }
    }

    private lateinit var binding: ActivityFailedRegistrationBinding
    private val openTicketViewModel: OpenTicketViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFailedRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    override fun onBackPressed() {
        startActivity(HomeActivity.newInstance(this))
    }

    private fun setupListeners() {
        setClickForTryAgainButton()
        setClickForBackHomeButton()
    }

    private fun setClickForTryAgainButton() {
        binding.activityFailedRegistrationTryAgainButton.setOnClickListener {
            val ticketTitle = intent.getStringExtra(TICKET_TITLE_EXTRA)
            val answersToSave: HashMap<*, *> =
                intent.extras?.get(ANSWERS_TO_SAVE_EXTRA) as HashMap<*, *>
            if (ticketTitle != null) {
                openTicketViewModel.createTicket(
                    ticketTitle,
                    answersToSave as HashMap<String, String>
                )
            }
        }
    }

    private fun setClickForBackHomeButton() {
        binding.activityFailedRegistrationBackHomeButton.setOnClickListener {
            startActivity(HomeActivity.newInstance(this))
        }
    }

    private fun setupObservers() {
        setupStateObserver()
        setupEventObserver()
    }

    private fun setupStateObserver() {
        openTicketViewModel.state.observe(this, Observer {
            if (it is OpenTicketState.ShowLoading) {
                binding.activityFailedRegistrationViewGroup.visibility = View.GONE
                binding.activityFailedRegistrationProgressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun setupEventObserver() {
        openTicketViewModel.event.observe(this, Observer {
            when (it) {
                is OpenTicketEvent.OpenLoginScreen -> {
                    startActivity(LoginActivity.newInstance(this))
                }
                is OpenTicketEvent.OpenFailedRegistrationScreen -> {
                    binding.activityFailedRegistrationViewGroup.visibility = View.VISIBLE
                    binding.activityFailedRegistrationProgressBar.visibility = View.GONE
                }
                is OpenTicketEvent.OpenRegistrationScreenSuccessfully -> {
                    startActivity(RegistrationSuccessfullyActivity.newInstance(this))
                }
            }
        })
    }
}
