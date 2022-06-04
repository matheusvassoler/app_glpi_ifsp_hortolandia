package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    companion object {
        private const val WEB_VIEW_CONTENT = "web_view_content"
        fun newInstance(context: Context, webViewContent: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(WEB_VIEW_CONTENT, webViewContent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        val webViewContent = intent.getStringExtra(WEB_VIEW_CONTENT)
        webViewContent?.let { binding.webView.loadData(it, "text/html", "UTF-8") }
    }

    private fun setToolbar() {
        binding.activityWebViewToolbar.toolbarTitle.text = getString(R.string.ticket_details_toolbar_title)
        binding.activityWebViewToolbar.toolbarLeftIcon.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.back_button_icon, null)
        )
        binding.activityWebViewToolbar.toolbarLeftIcon.setOnClickListener {
            finish()
        }
    }
}
