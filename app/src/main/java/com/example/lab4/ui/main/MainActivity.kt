package com.example.lab4.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.lab4.R
import com.example.lab4.databinding.ActivityMainBinding
import com.example.lab4.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun inflateBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun observeViewModel() = Unit

    override fun createAfterInflateBindingSetupObserverViewModel(savedInstanceState: Bundle?) {
        configNavigation()
    }

    override fun configureToolbarAndConfigScreenSections() = Unit

    private fun configNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}