package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.databinding.ActivityRegistrationSuccessfullyBinding

class RegistrationSuccessfullyActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, RegistrationSuccessfullyActivity::class.java)
        }
    }

    private lateinit var binding: ActivityRegistrationSuccessfullyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationSuccessfullyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityRegistrationSuccessfullyButton.setOnClickListener {
            startActivity(HomeActivity.newInstance(this))
        }
    }

    override fun onBackPressed() {
        startActivity(HomeActivity.newInstance(this))
    }
}
