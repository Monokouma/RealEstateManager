package com.despaircorp.ui.main.main_activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityMainBinding
import com.despaircorp.ui.databinding.AddAgentPopUpBinding
import com.despaircorp.ui.databinding.AddingChoicePopUpBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.login.LoginActivity
import com.despaircorp.ui.main.details_fragment.DetailFragment
import com.despaircorp.ui.main.estate_addition.CreateEstateActivity
import com.despaircorp.ui.main.loan_simulator.LoanSimulatorActivity
import com.despaircorp.ui.main.master_fragment.MasterFragment
import com.despaircorp.ui.map.MapActivity
import com.despaircorp.ui.utils.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MasterFragment.OnItemSelectedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel: MainViewModel by viewModels()
    private var twoPane: Boolean = false
    private var currentSelectedItemId: Int = 1
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        requestLocationPermission()
        
        setSupportActionBar(binding.activityMainToolbarRoot)
        
        twoPane = binding.activityMainFragmentLayoutDetails != null
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_FragmentLayout_master, MasterFragment())
                .commit()
            
            if (twoPane) {
                val detailsFragment = DetailFragment.newInstance(currentSelectedItemId)
                
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_FragmentLayout_details, detailsFragment)
                    .commit()
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_FragmentLayout_master, MasterFragment())
                .commit()
            if (twoPane) {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                }
                val detailsFragment =
                    DetailFragment.newInstance(savedInstanceState.getInt("selectedItemId", 1))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_FragmentLayout_details, detailsFragment)
                    .commit()
            }
        }
        
        
        val headerBinding = HeaderNavigationDrawerBinding.bind(
            binding.activityMainNavigationViewProfile.getHeaderView(0)
        )
        
        
        binding.activityMainToolbarRoot.setNavigationOnClickListener {
            binding.activityMainDrawerLayout.open()
        }
        
        
        binding.activityMainNavigationViewProfile.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.drawer_menu_change_currency -> {
                    viewModel.onChangeCurrencyClicked()
                    binding.activityMainDrawerLayout.close()
                }
                
                R.id.drawer_menu_map_view -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startActivity(MapActivity.navigate(this))
                        binding.activityMainDrawerLayout.close()
                    } else {
                        Toast.makeText(
                            this,
                            getString(com.despaircorp.shared.R.string.permission_map_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.activityMainDrawerLayout.close()
                    }
                    
                }
                
                R.id.drawer_menu_loan_simulator -> {
                    startActivity(LoanSimulatorActivity.navigate(this))
                    binding.activityMainDrawerLayout.close()
                }
                
                R.id.drawer_menu_logout -> {
                    viewModel.onDisconnect()
                }
            }
            
            true
        }
        
        viewModel.viewAction.observe(this) {
            when (val action = it.getContentIfNotHandled()) {
                is MainViewAction.AgentCreationSuccess -> {
                    Toast.makeText(this, action.message, Toast.LENGTH_SHORT).show()
                }
                
                is MainViewAction.Error -> {
                    Toast.makeText(this, action.message, Toast.LENGTH_SHORT).show()
                }
                
                MainViewAction.SuccessDisconnection -> {
                    startActivity(LoginActivity.navigate(this))
                    finish()
                }
                
                else -> Unit
            }
        }
        
        viewModel.viewState.observe(this) {
            Glide.with(headerBinding.navigationDrawerImageViewUserImage)
                .load(it.currentLoggedInAgent.imageResource)
                .into(headerBinding.navigationDrawerImageViewUserImage)
            
            headerBinding.navigationDrawerTextViewUserName.text = it.currentLoggedInAgent.name
        }
        
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    // Both permissions were granted
                }
                return
            }
            
            else -> {
                // Ignore other requests
            }
        }
    }
    
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", currentSelectedItemId)
    }
    
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.top_menu_add -> {
                val popUpBinding by viewBinding { AddingChoicePopUpBinding.inflate(it) }
                
                val dialog =
                    MaterialAlertDialogBuilder(
                        this,
                        R.style.ThemeOverlay_MaterialAlertDialog_Rounded
                    )
                        .setView(popUpBinding.root)
                        .create()
                
                dialog.show()
                
                popUpBinding.addingChoicePopUpCardViewEstate.setOnClickListener {
                    
                    startActivity(CreateEstateActivity.navigate(this))
                    
                    dialog.cancel()
                }
                
                popUpBinding.addingChoicePopUpCardViewAgent.setOnClickListener {
                    onAddAgentClicked()
                    
                    
                    dialog.cancel()
                }
                
            }
            
            R.id.top_menu_edit -> {
            
            }
            
            R.id.top_menu_search -> {
            
            }
            
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }
    
    private fun onAddAgentClicked() {
        val popUpBinding by viewBinding { AddAgentPopUpBinding.inflate(it) }
        
        val dialog =
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialAlertDialog_Rounded)
                .setView(popUpBinding.root)
                .create()
        
        dialog.show()
        
        popUpBinding.addAgentPopUpTextInputEditTextGoal.addTextChangedListener {
            viewModel.onAgentNameTextChanged(it.toString())
        }
        
        popUpBinding.addAgentPopUpButtonCreate.setOnClickListener {
            viewModel.onCreateAgentClick()
            dialog.cancel()
        }
    }
    
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        
        fun navigate(context: Context) = Intent(
            context,
            MainActivity::class.java
        )
    }
    
    override fun onItemSelected(itemId: Int) {
        currentSelectedItemId = itemId
        if (twoPane) {
            val detailsFragment = DetailFragment.newInstance(itemId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_FragmentLayout_details, detailsFragment)
                .commit()
        } else {
            val detailsFragment = DetailFragment.newInstance(itemId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_FragmentLayout_master, detailsFragment)
                .addToBackStack("detail")
                .commit()
        }
    }
    
    
}