package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListenerToTryAgainButton()
    }

    private fun setListenerToTryAgainButton() {
        binding.activityErrorTryAgainButton.setOnClickListener {
            startHomeActivity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startHomeActivity()
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
