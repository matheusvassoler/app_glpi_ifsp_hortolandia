package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityOpenTicketBinding
import com.glpi.ifsp.hortolandia.ui.fragment.OpenTicketTutorialFragment

class OpenTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpenTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachFragmentToActivity()
        setToolbar()
    }

    private fun attachFragmentToActivity() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.activity_open_ticket_container, OpenTicketTutorialFragment(), "open_ticket_fragment")
        }.commit()
    }

    private fun setToolbar() {
        binding.activityOpenTicketToolbar.toolbarTitle.text =
            getString(R.string.open_ticket_toolbar_title)
        binding.activityOpenTicketToolbar.toolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.back_button_icon, null)
        binding.activityOpenTicketToolbar.toolbar.setNavigationOnClickListener {
            goToHomeActivity()
        }
    }

    private fun goToHomeActivity() {
        val myFragment = supportFragmentManager.findFragmentByTag("open_ticket_fragment")
        if (checkIfOpenTicketTutorialFragmentIsVisible(myFragment)) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun checkIfOpenTicketTutorialFragmentIsVisible(myFragment: Fragment?) =
        myFragment != null && myFragment.isVisible

    override fun onBackPressed() {
        super.onBackPressed()
        goToHomeActivity()
    }
}
