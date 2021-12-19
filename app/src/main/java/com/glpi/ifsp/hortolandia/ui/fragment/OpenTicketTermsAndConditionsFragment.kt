package com.glpi.ifsp.hortolandia.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.FragmentOpenTicketTermsAndConditionsBinding

class OpenTicketTermsAndConditionsFragment : Fragment() {

    private var _binding: FragmentOpenTicketTermsAndConditionsBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            description = it.getString("description")
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

        val htmlCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }

        description?.let {
            binding.fragmentOpenTicketTermsAndConditionsWebView.loadData(
                getDataForWebView(htmlCode), "text/html", "UTF-8"
            )
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(title: String, description: String) =
            OpenTicketTermsAndConditionsFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("description", description)
                }
            }

        private const val APP_BAR_TITLE = "Abrir chamado"
    }
}
