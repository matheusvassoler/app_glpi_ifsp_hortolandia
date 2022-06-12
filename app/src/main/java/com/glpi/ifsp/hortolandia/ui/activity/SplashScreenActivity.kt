package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
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
                SplashScreenEvent.OpenLogin -> {
                    startActivity(LoginActivity.newInstance(this))
                }
                SplashScreenEvent.OpenHome -> {
                    startActivity(HomeActivity.newInstance(this))
                }
            }
        })
    }
}
