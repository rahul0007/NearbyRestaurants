package com.nearby.task.ui.activity.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.nearby.task.R
import com.nearby.task.ui.base.BaseActivity
import com.nearby.task.viewModel.NearByViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class HomeActivity : BaseActivity() {

    lateinit var navController: NavController
    lateinit var homeViewModel: NearByViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun findFragmentPlaceHolder() = 0
    override fun findContentView() = R.layout.activity_home
    override fun observeViewModel() {
        navController = findNavController(this, R.id.activity_main_nav_host_fragment)
        homeViewModel = ViewModelProvider(this)[NearByViewModel::class.java]
        NavigationUI.setupWithNavController(activity_main_bottom_navigation_view, navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_near_by -> {
                    textViewTitle.text = resources.getString(R.string.near_by_restaurant)
                }
                R.id.navigation_wishlist -> {
                    textViewTitle.text = resources.getString(R.string.wish_list)
                }
            }
        }

    }


}