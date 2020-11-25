package com.staqoapp.appScreens

import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.staqoapp.R
import com.staqoapp.databinding.MainActivityBinding
import com.staqoapp.utils.Constants.OtherConstants.ERROR
import com.staqoapp.utils.Constants.OtherConstants.LOADING
import com.staqoapp.utils.Constants.OtherConstants.NO_USER_FOUND
import com.staqoapp.utils.Constants.OtherConstants.SUCCESS
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: MainActivityBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.selectionFragment, R.id.horizontalListFragment, R.id.verticalListFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        viewModel.getUsers()
        viewModel.responseStatus.observe(this, {
            when (it) {
                ERROR -> {
                    hideProgress()
                    Snackbar.make(
                        mBinding.container,
                        getString(R.string.something_went_wrong),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                NO_USER_FOUND -> {
                    hideProgress()
                    Snackbar.make(
                        mBinding.container,
                        getString(R.string.no_user_list_found),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                SUCCESS -> {
                    hideProgress()
                }
            }
        })
    }

    private fun hideProgress() {
        mBinding.progressBar.visibility = GONE
    }
}