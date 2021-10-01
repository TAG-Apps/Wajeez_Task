package com.wajeez.sample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.FirebaseApp
import com.wajeez.sample.R
import com.wajeez.sample.databinding.ActivityMainBinding
import com.wajeez.sample.model.interfaces.OnProgressLoadingListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),OnProgressLoadingListener {

    private val mBinding :ActivityMainBinding by viewBinding()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)


        val navHost = supportFragmentManager.findFragmentById(R.id.nav_id) as NavHostFragment
        navController = navHost.findNavController()


        setSupportActionBar(mBinding.toolbar)
        setupActionBarWithNavController(navController)

        mBinding.toolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            run {

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (navController.currentDestination?.id == R.id.mainFragment)
            return false
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun showProgress() {
        mBinding.waitingProgress.isVisible = true
    }

    override fun hideProgress() {
        mBinding.waitingProgress.isVisible = false
    }


}