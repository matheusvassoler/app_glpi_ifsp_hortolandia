package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun attachFragmentToActivity() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.activity_open_ticket_container, OpenTicketTutorialFragment(), "open_ticket_fragment")
        }.commit()
    }
}
