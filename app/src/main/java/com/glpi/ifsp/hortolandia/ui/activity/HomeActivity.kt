package com.glpi.ifsp.hortolandia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glpi.ifsp.hortolandia.R
import com.glpi.ifsp.hortolandia.databinding.ActivityHomeBinding
import com.glpi.ifsp.hortolandia.ui.fragment.ProfileFragment
import com.glpi.ifsp.hortolandia.ui.fragment.TicketFragment

class HomeActivity : AppCompatActivity() {

    companion object {
        private const val PROFILE_FRAGMENT_TAG = "ProfileFragment"
        private const val HOME_FRAGMENT_TAG = "TicketFragment"

        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitialFragment()
        setListenersToBottomNavigationView()
    }

    private fun setInitialFragment() {
        initHomeFragment()
    }

    private fun setListenersToBottomNavigationView() {
        setOnNavigationItemSelectedListener()
        setOnNavigationItemReselectedListener()
    }

    private fun setOnNavigationItemSelectedListener() {
        binding.activityHomeBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_menu_home -> initHomeFragment()
                R.id.bottom_nav_menu_new_ticket -> goToOpenTicketActivity()
                R.id.bottom_nav_menu_profile -> initProfileFragment()
            }
            true
        }
    }

    private fun setOnNavigationItemReselectedListener() {
        binding.activityHomeBottomNavigationView.setOnNavigationItemReselectedListener {
            // Nothing here to disable reselect
        }
    }

    private fun initHomeFragment() {
        setToolbarTitle(getString(R.string.ticket_toolbar_title))
        replaceFragment(TicketFragment(), HOME_FRAGMENT_TAG)
    }

    private fun goToOpenTicketActivity() {
        val intent = Intent(this, OpenTicketActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun initProfileFragment() {
        setToolbarTitle(getString(R.string.profile_toolbar_title))
        replaceFragment(ProfileFragment(), PROFILE_FRAGMENT_TAG)
    }

    private fun replaceFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view, fragment, fragmentTag)
        }.commit()
    }

    private fun setToolbarTitle(title: String) {
        binding.activityHomeToolbar.toolbarTitle.text = title
    }
}
