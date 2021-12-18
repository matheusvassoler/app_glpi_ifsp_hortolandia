package com.glpi.ifsp.hortolandia.ui.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        binding.fragmentOpenTicketFormToolbar.toolbarTitle.text = "Abrir chamado"
        binding.fragmentOpenTicketTermsAndConditionsTitle.text = title

        val teste = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }

        description?.let { binding.fragmentOpenTicketTermsAndConditionsWebView.loadData("<html><head><style>h3 {font-weight: normal; font-size: 15px}</style></head><body>$teste</body></html>", "text/html", "UTF-8"); }
    }

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
    }
}
