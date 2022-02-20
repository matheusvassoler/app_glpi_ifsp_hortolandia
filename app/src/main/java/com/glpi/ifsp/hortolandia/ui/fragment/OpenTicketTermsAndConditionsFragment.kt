package com.glpi.ifsp.hortolandia.ui.fragment

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketTermsAndConditionsBinding
import com.glpi.ifsp.hortolandia.ui.activity.FailedRegistrationActivity
import com.glpi.ifsp.hortolandia.ui.activity.LoginActivity
import com.glpi.ifsp.hortolandia.ui.activity.RegistrationSuccessfullyActivity
import com.glpi.ifsp.hortolandia.ui.event.OpenTicketEvent
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.glpi.ifsp.hortolandia.ui.viewmodel.OpenTicketViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class OpenTicketTermsAndConditionsFragment : Fragment() {

    private val openTicketViewModel: OpenTicketViewModel by viewModel()
    private var _binding: FragmentOpenTicketTermsAndConditionsBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var description: String? = null
    private var ticketTitle: String? = null
    private var answersToSave: HashMap<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TERMS_TITLE_EXTRA)
            description = it.getString(TERMS_DESCRIPTION)
            ticketTitle = it.getString(TICKET_TITLE_EXTRA)
            answersToSave = it.getSerializable(ANSWERS_TO_SAVE_EXTRA) as HashMap<String, String>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenTicketTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentOpenTicketFormToolbar.toolbarTitle.text =
            getString(R.string.toolbar_title_open_ticket)
        binding.fragmentOpenTicketTermsAndConditionsTitle.text = title

        val htmlCode = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)

        description?.let {
            binding.fragmentOpenTicketTermsAndConditionsWebView.loadData(
                getDataForWebView(htmlCode), "text/html", "UTF-8"
            )
        }

        binding.fragmentOpenTicketTermsAndConditionsButton.isEnabled = false

        setupObservers()
        setListeners()
    }

    private fun setListeners() {
        setListenerForCreateTicketButton()
        setListenerForCheckbox()
    }

    private fun setListenerForCreateTicketButton() {
        binding.fragmentOpenTicketTermsAndConditionsButton.setOnClickListener {
            ticketTitle?.let { title ->
                answersToSave?.let { answers ->
                    openTicketViewModel.createTicket(title, answers)
                }
            }
        }
    }

    private fun setListenerForCheckbox() {
        binding.fragmentOpenTicketTermsAndConditionsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableCreateTicketButton()
            } else {
                disableCreateTicketButton()
            }
        }
    }

    private fun disableCreateTicketButton() {
        binding.fragmentOpenTicketTermsAndConditionsButton.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        binding.fragmentOpenTicketTermsAndConditionsButtonText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green_dark
            )
        )
        binding.fragmentOpenTicketTermsAndConditionsButton.strokeColor =
            ContextCompat.getColor(requireContext(), R.color.green_dark)
        binding.fragmentOpenTicketTermsAndConditionsButton.isEnabled = false
        binding.fragmentOpenTicketTermsAndConditionsButton.isClickable = false
    }

    private fun enableCreateTicketButton() {
        binding.fragmentOpenTicketTermsAndConditionsButton.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.green_dark)
        )
        binding.fragmentOpenTicketTermsAndConditionsButtonText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.fragmentOpenTicketTermsAndConditionsButton.strokeColor =
            ContextCompat.getColor(requireContext(), R.color.green_dark)
        binding.fragmentOpenTicketTermsAndConditionsButton.isEnabled = true
        binding.fragmentOpenTicketTermsAndConditionsButton.isClickable = true
    }

    private fun getDataForWebView(htmlCode: Spanned?) =
        """
            <html>
                <head>
                    <style>
                        h3 {font-weight: normal; font-size: 15px}
                    </style>
                </head>
                <body>
                    $htmlCode
                </body>
            </html>
        """

    private fun setupObservers() {
        setupStateObserver()
        setupEventObserver()
    }

    private fun setupStateObserver() {
        openTicketViewModel.state.observe(viewLifecycleOwner, Observer {
            if (it is OpenTicketState.ShowLoading) {
                binding.fragmentOpenTicketTermsAndConditionsViewGroup.visibility = View.GONE
                binding.fragmentOpenTicketTermsAndConditionsProgressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun setupEventObserver() {
        openTicketViewModel.event.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OpenTicketEvent.OpenLoginScreen -> {
                    startActivity(LoginActivity.newInstance(requireContext()))
                }
                is OpenTicketEvent.OpenFailedRegistrationScreen -> {
                    startFailedRegistrationActivity(it)
                }
                is OpenTicketEvent.OpenRegistrationScreenSuccessfully -> {
                    startActivity(RegistrationSuccessfullyActivity.newInstance(requireContext()))
                }
            }
        })
    }

    private fun startFailedRegistrationActivity(event: OpenTicketEvent.OpenFailedRegistrationScreen) {
        startActivity(
            FailedRegistrationActivity.newInstance(
                requireContext(),
                event.ticketTitle,
                event.answersToSave
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TERMS_TITLE_EXTRA = "title"
        private const val TERMS_DESCRIPTION = "description"
        private const val ANSWERS_TO_SAVE_EXTRA = "ANSWERS_TO_SAVE_EXTRA"
        private const val TICKET_TITLE_EXTRA = "TICKET_TITLE_EXTRA"

        fun newInstance(
            title: String,
            description: String,
            ticketTitle: String,
            answersToSave: HashMap<String, String>
        ) =
            OpenTicketTermsAndConditionsFragment().apply {
                arguments = Bundle().apply {
                    putString(TERMS_TITLE_EXTRA, title)
                    putString(TERMS_DESCRIPTION, description)
                    putString(TICKET_TITLE_EXTRA, ticketTitle)
                    putSerializable(ANSWERS_TO_SAVE_EXTRA, answersToSave)
                }
            }
    }
}
