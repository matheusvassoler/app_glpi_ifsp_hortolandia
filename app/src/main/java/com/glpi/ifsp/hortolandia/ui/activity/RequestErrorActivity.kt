package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.databinding.ActivityRequestErrorBinding

class RequestErrorActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, RequestErrorActivity::class.java)
        }
    }

    private lateinit var binding: ActivityRequestErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityRequestErrorButton.setOnClickListener {
            startActivity(HomeActivity.newInstance(this))
        }
    }

    override fun onBackPressed() {
        startActivity(HomeActivity.newInstance(this))
    }
}
