package com.glpi.ifsp.hortolandia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityHomeBinding
import com.glpi.ifsp.hortolandia.ui.fragment.ProfileFragment
import com.glpi.ifsp.hortolandia.ui.fragment.TicketFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitialFragment()
        setListenersToBottomNavigationView()
    }

    private fun setInitialFragment() {
        setCurrentFragment(TicketFragment(), HOME_FRAGMENT_TAG)
    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment, fragmentTag)
        }.commit()
    }

    private fun setListenersToBottomNavigationView() {
        setOnNavigationItemSelectedListener()
        setOnNavigationItemReselectedListener()
    }

    private fun setOnNavigationItemSelectedListener() {
        binding.activityHomeBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_menu_home -> setCurrentFragment(TicketFragment(), HOME_FRAGMENT_TAG)
                R.id.bottom_nav_menu_profile -> setCurrentFragment(
                    ProfileFragment(),
                    PROFILE_FRAGMENT_TAG
                )
            }
            true
        }
    }

    private fun setOnNavigationItemReselectedListener() {
        binding.activityHomeBottomNavigationView.setOnNavigationItemReselectedListener {
            // Nothing here to disable reselect
        }
    }

    companion object {
        private const val PROFILE_FRAGMENT_TAG = "ProfileFragment"
        private const val HOME_FRAGMENT_TAG = "TicketFragment"
    }
}
