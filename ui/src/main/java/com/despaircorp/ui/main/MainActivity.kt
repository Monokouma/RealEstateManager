package com.despaircorp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityMainBinding
import com.despaircorp.ui.databinding.AddAgentPopUpBinding
import com.despaircorp.ui.databinding.AddingChoicePopUpBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.login.LoginActivity
import com.despaircorp.ui.utils.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel: MainViewModel by viewModels()
    
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.activityMainToolbarRoot)
        
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFragmentContainerView.id)
                    as NavHostFragment
        navController = navHostFragment.findNavController()
        
        navController.addOnDestinationChangedListener(this)
        
        
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
                    onAddEstateClicked()
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
    
    private fun onAddEstateClicked() {
    
    }
    
    companion object {
        
        fun navigate(context: Context) = Intent(
            context,
            MainActivity::class.java
        )
    }
    
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
    
    
    }
}