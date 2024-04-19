package com.despaircorp.ui.main.main_activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.filter.FilterTypeEnum
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.ActivityMainBinding
import com.despaircorp.ui.databinding.AddAgentPopUpBinding
import com.despaircorp.ui.databinding.AddingChoicePopUpBinding
import com.despaircorp.ui.databinding.HeaderNavigationDrawerBinding
import com.despaircorp.ui.login.LoginActivity
import com.despaircorp.ui.main.details_fragment.DetailFragment
import com.despaircorp.ui.main.estate_form.EstateFormActivity
import com.despaircorp.ui.main.estate_form.estate_type.EstateTypeAdapter
import com.despaircorp.ui.main.estate_form.estate_type.EstateTypeListener
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestAdapter
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestListener
import com.despaircorp.ui.main.loan_simulator.LoanSimulatorActivity
import com.despaircorp.ui.main.master_fragment.MasterFragment
import com.despaircorp.ui.map.MapActivity
import com.despaircorp.ui.utils.isNightMode
import com.despaircorp.ui.utils.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MasterFragment.OnItemSelectedListener,
    PointOfInterestListener, EstateTypeListener {
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
        
        
        val pointOfInterestAdapter = PointOfInterestAdapter(this)
        binding.activityMainRecyclerViewInterestPoint.adapter = pointOfInterestAdapter
        
        val estateTypeAdapter = EstateTypeAdapter(this)
        binding.activityMainRecyclerViewTypeRoot.adapter = estateTypeAdapter
        
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
            changeVisibilityWithAnimation(it.isFilterMenuVisible)
            pointOfInterestAdapter.submitList(it.pointOfInterestViewStateItems)
            estateTypeAdapter.submitList(it.estateTypeViewStateItems)
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
                    
                    startActivity(
                        EstateFormActivity.navigate(
                            context = this,
                            isEditMode = false,
                            toEditEstateId = currentSelectedItemId
                        )
                    )
                    
                    dialog.cancel()
                }
                
                popUpBinding.addingChoicePopUpCardViewAgent.setOnClickListener {
                    onAddAgentClicked()
                    
                    
                    dialog.cancel()
                }
                
            }
            
            R.id.top_menu_edit -> {
                startActivity(
                    EstateFormActivity.navigate(
                        this,
                        true,
                        currentSelectedItemId
                    )
                )
            }
            
            R.id.top_menu_search -> {
                viewModel.onSearchMenuClicked()
                
                val myListFragment =
                    supportFragmentManager.findFragmentById(R.id.activity_main_FragmentLayout_master) as? MasterFragment
                onFilterSoldAndForSaleButtonClick(myListFragment)
                onFilterRoomNumberChanged(myListFragment)
                onSurfaceMinTextChanged(myListFragment)
                onSurfaceMaxTextChanged(myListFragment)
                onPriceMinTextChanged(myListFragment)
                onPriceMaxTextChanged(myListFragment)
                onEntryDateClicked(myListFragment)
                
                binding.activityMainButtonApplyFilter.setOnClickListener {
                    myListFragment?.onEstateTypeForFilteringChanged(viewModel.estateTypeListMutableStateFlow.value)
                    myListFragment?.onPointOfInterestForFilteringChanged(viewModel.pointOfInterestListMutableStateFlow.value)
                    myListFragment?.onApplyFilter()
                }
                
                binding.activityMainButtonResetFilter.setOnClickListener {
                    viewModel.resetList()
                    myListFragment?.onResetFilter()
                    binding.activityMainTextInputEditTextRoom.setText("")
                    binding.activityMainButtonEntryDate.text =
                        getString(com.despaircorp.shared.R.string.entry_date)
                    binding.activityMainTextInputEditTextPriceMin.setText("")
                    binding.activityMainTextInputEditTextPriceMax.setText("")
                    binding.activityMainTextInputEditTextSurfaceMin.setText("")
                    binding.activityMainTextInputEditTextSurfaceMax.setText("")
                    if (isNightMode(this)) {
                        binding.activityMainButtonForSale.setBackgroundColor(
                            getColor(R.color.unselectButtonDark)
                        )
                        
                        binding.activityMainButtonSold.setBackgroundColor(
                            getColor(R.color.unselectButtonDark)
                        )
                    } else {
                        binding.activityMainButtonForSale.setBackgroundColor(
                            getColor(R.color.unselectButtonLight)
                        )
                        
                        binding.activityMainButtonSold.setBackgroundColor(
                            getColor(R.color.unselectButtonLight)
                        )
                    }
                }
            }
            
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun onEntryDateClicked(myListFragment: MasterFragment?) {
        binding.activityMainButtonEntryDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(com.despaircorp.shared.R.string.select_date))
                    .build()
            
            datePicker.addOnPositiveButtonClickListener {
                val localDate =
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                
                binding.activityMainButtonEntryDate.text = localDate
                myListFragment?.onFilterChangedListener(localDate, FilterTypeEnum.ENTRY_DATE)
                
            }
            
            datePicker.show(supportFragmentManager, "entry_date");
        }
    }
    
    private fun onPriceMinTextChanged(myListFragment: MasterFragment?) {
        binding.activityMainTextInputEditTextPriceMin.addTextChangedListener {
            myListFragment?.onFilterChangedListener(
                it.toString(),
                FilterTypeEnum.PRICE_MIN
            )
        }
    }
    
    private fun onPriceMaxTextChanged(myListFragment: MasterFragment?) {
        binding.activityMainTextInputEditTextPriceMax.addTextChangedListener {
            myListFragment?.onFilterChangedListener(
                it.toString(),
                FilterTypeEnum.PRICE_MAX
            )
        }
    }
    
    private fun onSurfaceMinTextChanged(myListFragment: MasterFragment?) {
        binding.activityMainTextInputEditTextSurfaceMax.addTextChangedListener {
            myListFragment?.onFilterChangedListener(
                it.toString(),
                FilterTypeEnum.SURFACE_MAX
            )
        }
    }
    
    private fun onSurfaceMaxTextChanged(myListFragment: MasterFragment?) {
        binding.activityMainTextInputEditTextSurfaceMin.addTextChangedListener {
            myListFragment?.onFilterChangedListener(
                it.toString(),
                FilterTypeEnum.SURFACE_MIN
            )
        }
    }
    
    private fun onFilterRoomNumberChanged(myListFragment: MasterFragment?) {
        binding.activityMainTextInputEditTextRoom.addTextChangedListener {
            myListFragment?.onFilterChangedListener(
                it.toString(),
                FilterTypeEnum.ROOM_NUMBER
            )
        }
    }
    
    private fun onFilterSoldAndForSaleButtonClick(myListFragment: MasterFragment?) {
        binding.activityMainButtonForSale.setOnClickListener {
            myListFragment?.onFilterChangedListener(
                EstateStatus.FOR_SALE.name,
                FilterTypeEnum.FOR_SALE
            )
            
            if (isNightMode(this)) {
                binding.activityMainButtonForSale.setBackgroundColor(
                    getColor(R.color.primaryColorDark)
                )
                
                binding.activityMainButtonSold.setBackgroundColor(
                    getColor(R.color.unselectButtonDark)
                )
            } else {
                binding.activityMainButtonForSale.setBackgroundColor(
                    getColor(R.color.primaryColorLight)
                )
                
                binding.activityMainButtonSold.setBackgroundColor(
                    getColor(R.color.unselectButtonLight)
                )
            }
        }
        
        binding.activityMainButtonSold.setOnClickListener {
            myListFragment?.onFilterChangedListener(
                EstateStatus.SOLD.name,
                FilterTypeEnum.SOLD
            )
            if (isNightMode(this)) {
                binding.activityMainButtonSold.setBackgroundColor(
                    getColor(R.color.primaryColorDark)
                )
                
                binding.activityMainButtonForSale.setBackgroundColor(
                    getColor(R.color.unselectButtonDark)
                )
            } else {
                binding.activityMainButtonSold.setBackgroundColor(
                    getColor(R.color.primaryColorLight)
                )
                
                binding.activityMainButtonForSale.setBackgroundColor(
                    getColor(R.color.unselectButtonLight)
                )
            }
        }
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
    
    private fun changeVisibilityWithAnimation(isFilterMenuVisible: Boolean) {
        TransitionManager.endTransitions(binding.root)
        
        TransitionManager.beginDelayedTransition(binding.root)
        if (isFilterMenuVisible) {
            binding.activityMainConstraintLayoutRootFilter.visibility = View.VISIBLE
        } else {
            binding.activityMainConstraintLayoutRootFilter.visibility = View.GONE
        }
    }
    
    
    override fun onAddPointOfInterestClicked(id: Int) {
        viewModel.onAddPointOfInterest(id)
    }
    
    override fun onRemovePointOfInterestClicked(id: Int) {
        viewModel.onRemovePointOfInterest(id)
    }
    
    override fun onAddEstateTypeClicked(id: Int) {
        viewModel.onAddEstateType(id)
    }
    
    override fun onRemoveEstateTypeClicked(id: Int) {
        viewModel.onRemoveEstateType(id)
    }
    
    override fun onTypeClicked(id: Int) {}
    
    
}