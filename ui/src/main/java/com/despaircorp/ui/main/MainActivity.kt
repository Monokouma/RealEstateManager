package com.despaircorp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityMainBinding
import com.despaircorp.ui.databinding.AddAgentPopUpBinding
import com.despaircorp.ui.databinding.AddEstatePopUpBinding
import com.despaircorp.ui.databinding.AddingChoicePopUpBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.login.LoginActivity
import com.despaircorp.ui.main.details_fragment.DetailFragment
import com.despaircorp.ui.main.master_fragment.MasterFragment
import com.despaircorp.ui.utils.uriToBitmap
import com.despaircorp.ui.utils.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MasterFragment.OnItemSelectedListener {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }
    private val viewModel: MainViewModel by viewModels()
    private var twoPane: Boolean = false
    private var currentSelectedItemId: Int = 1
    
    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    showImageTypeDialog(uri)
                }
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
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
    
    override fun onResume() {
        super.onResume()
        
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
        val popUpBinding by viewBinding { AddEstatePopUpBinding.inflate(it) }
        
        val dialog =
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialAlertDialog_Rounded)
                .setView(popUpBinding.root)
                .create()
        
        dialog.show()
        
        popUpBinding.addEstatePopUpButtonAddPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageResultLauncher.launch(intent)
        }
    }
    
    private fun showImageTypeDialog(imageUri: Uri) {
        val imageTypes = EstatePictureType.entries.toTypedArray()
        val items = imageTypes.map { it.name.capitalize(Locale.getDefault()) }.toTypedArray()
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Image Type")
            .setItems(items) { _, which ->
                val selectedType = imageTypes[which]
                uriToBitmap(this, imageUri)
                
            }
            .show()
    }
    
    
    companion object {
        
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