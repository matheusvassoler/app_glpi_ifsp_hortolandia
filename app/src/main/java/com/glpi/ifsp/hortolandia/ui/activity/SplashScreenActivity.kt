package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.ui.event.SplashScreenEvent
import com.glpi.ifsp.hortolandia.ui.viewmodel.SplashScreenViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashScreenViewModel.checkIfThereIsAValidLogin()

        splashScreenViewModel.event.observe(this, {
            when (it) {
                SplashScreenEvent.OpenLogin -> goToLogin()
                SplashScreenEvent.OpenHome -> goToHome()
            }
        })
    }

    private fun goToLogin() {
        openScreen(LoginActivity.newInstance(this))
    }

    private fun goToHome() {
        openScreen(HomeActivity.newInstance(this))
    }

    private fun openScreen(intent: Intent) {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, THREE_SECONDS_SPLASH_SCREEN_DELAY)
    }

    companion object {
        private const val THREE_SECONDS_SPLASH_SCREEN_DELAY = 2000L
    }
}
