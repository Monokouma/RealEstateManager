package com.despaircorp.ui.main.estate_form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.shared.R
import com.despaircorp.ui.databinding.ActivityCreateEstateBinding
import com.despaircorp.ui.main.estate_form.agent.EstateFormAgentAdapter
import com.despaircorp.ui.main.estate_form.agent.EstateFormAgentListener
import com.despaircorp.ui.main.estate_form.picture.EstateFormPictureAdapter
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestAdapter
import com.despaircorp.ui.main.estate_form.point_of_interest.PointOfInterestListener
import com.despaircorp.ui.utils.isNightMode
import com.despaircorp.ui.utils.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class EstateFormActivity : AppCompatActivity(), PointOfInterestListener,
    EstateFormAgentListener {
    private val viewModel: EstateFormViewModel by viewModels()
    private val binding by viewBinding { ActivityCreateEstateBinding.inflate(it) }
    private var currentPhotoUri: Uri? = null
    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    showImageTypeDialog(uri)
                }
            }
        }
    
    private val takePictureCallback =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { successful ->
            if (successful) {
                showImageTypeDialog(currentPhotoUri ?: return@registerForActivityResult)
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        currentPhotoUri = savedInstanceState?.getParcelable(KEY_CURRENT_PHOTO_URI)
        
        val pointOfInterestAdapter = PointOfInterestAdapter(this)
        binding.addEstatePopUpRecyclerCiewInterestPoint.adapter = pointOfInterestAdapter
        
        val agentAdapter = EstateFormAgentAdapter(this)
        binding.activityCreateEstateRecyclerViewAgent.adapter = agentAdapter
        
        val createEstatePictureAdapter = EstateFormPictureAdapter()
        binding.activityCreateEstateRecyclerViewPicture.adapter = createEstatePictureAdapter
        
        initSpinnerDropDownSelectionChanged()
        initOnSurfaceTextChanged()
        initOnDescriptionTextChanged()
        onRoomTextChanged()
        onBedroomTextChanged()
        onBathroomTextChanged()
        onAddressTextChanged()
        onCityTextChanged()
        onPriceTextChanged()
        initEstateTypeSpinner()
        initSoldAndForSaleButton()
        initEntryDatePicker()
        initSoldDatePicker()
        imagePickerInit()
        takePictureInit()
        
        binding.addEstatePopUpButtonCreate.setOnClickListener {
            viewModel.onCreateEstateClicked()
        }
        
        viewModel.viewAction.observe(this) {
            when (val action = it.getContentIfNotHandled()) {
                is EstateFormAction.OnError -> Toast.makeText(
                    this,
                    action.message,
                    Toast.LENGTH_SHORT
                ).show()
                
                EstateFormAction.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.creation_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    finish()
                }
                
                else -> Unit
            }
        }
        
        viewModel.viewState.observe(this) {
            agentAdapter.submitList(it.agentViewStateItems)
            createEstatePictureAdapter.submitList(it.pictureViewStateItems)
            pointOfInterestAdapter.submitList(it.pointOfInterestViewStateItems)
            binding.addEstatePopUpTextInputEditTextSurface.setText(it.surface)
            binding.addEstatePopUpTextInputEditTextDescription.setText(it.description)
            binding.addEstatePopUpTextInputEditTextRoom.setText(it.roomNumber)
            binding.addEstatePopUpTextInputEditTextBedroom.setText(it.bedRoomNumber)
            binding.addEstatePopUpTextInputEditTextBathroom.setText(it.bathRoomNumber)
            binding.addEstatePopUpTextInputEditTextLocation.setText(it.address)
            binding.addEstatePopUpTextInputEditTextCity.setText(it.city)
            binding.addEstatePopUpTextInputEditTextPrice.setText(it.price)
            binding.activityCreateEstateButtonSoldDate.text = it.sellingDate
            binding.activityCreateEstateButtonEntryDate.text = it.entryDate
            reactOnSoldAndForSaleButton(it.isSoldEstate)
            
        }
    }
    
    private fun reactOnSoldAndForSaleButton(isSold: Boolean) {
        if (isSold) {
            //sold
            if (isNightMode(this)) {
                binding.addEstatePopUpButtonForSold.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.primaryColorDark)
                )
                
                binding.addEstatePopUpButtonForSale.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.unselectButtonDark)
                )
            } else {
                binding.addEstatePopUpButtonForSold.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.primaryColorLight)
                )
                
                binding.addEstatePopUpButtonForSale.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.unselectButtonLight)
                )
            }
            
            
        } else {
            //not sold
            if (isNightMode(this)) {
                binding.addEstatePopUpButtonForSale.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.primaryColorDark)
                )
                
                binding.addEstatePopUpButtonForSold.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.unselectButtonDark)
                )
            } else {
                binding.addEstatePopUpButtonForSale.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.primaryColorLight)
                )
                
                binding.addEstatePopUpButtonForSold.setBackgroundColor(
                    getColor(com.despaircorp.ui.R.color.unselectButtonLight)
                )
            }
        }
    }
    
    private fun onPriceTextChanged() {
        binding.addEstatePopUpTextInputEditTextPrice.addTextChangedListener {
            viewModel.onPriceTextChanged(it.toString())
        }
    }
    
    private fun onCityTextChanged() {
        binding.addEstatePopUpTextInputEditTextCity.addTextChangedListener {
            viewModel.onCityTextChanged(it.toString())
        }
    }
    
    private fun onAddressTextChanged() {
        binding.addEstatePopUpTextInputEditTextLocation.addTextChangedListener {
            viewModel.onLocationTextChanged(it.toString())
        }
    }
    
    private fun onRoomTextChanged() {
        
        binding.addEstatePopUpTextInputEditTextRoom.addTextChangedListener {
            viewModel.onRoomTextChanged(roomInput = it.toString())
        }
    }
    
    private fun onBedroomTextChanged() {
        binding.addEstatePopUpTextInputEditTextBedroom.addTextChangedListener {
            viewModel.onBedroomTextChanged(bedroomInput = it.toString())
            
        }
    }
    
    private fun onBathroomTextChanged() {
        binding.addEstatePopUpTextInputEditTextBathroom.addTextChangedListener {
            viewModel.onBathroomTextChanged(bathroomInput = it.toString())
            
        }
    }
    
    private fun initOnDescriptionTextChanged() {
        binding.addEstatePopUpTextInputEditTextDescription.addTextChangedListener {
            viewModel.onDescriptionTextChanged(onDescriptionInput = it.toString())
        }
    }
    
    private fun initOnSurfaceTextChanged() {
        binding.addEstatePopUpTextInputEditTextSurface.addTextChangedListener {
            viewModel.onSurfaceTextChanged(surfaceInput = it.toString())
        }
    }
    
    private fun initSpinnerDropDownSelectionChanged() {
        binding.addEstatePopUpSpinnerEstateType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (view != null) {
                        val selectedItem = parent.getItemAtPosition(position)
                        viewModel.onSpinnerSelectionChanged(selectedItem)
                    }
                }
                
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        
    }
    
    private fun initEstateTypeSpinner() {
        val items = listOf(
            getString(com.despaircorp.shared.R.string.manor),
            getString(com.despaircorp.shared.R.string.house),
            getString(com.despaircorp.shared.R.string.apartment),
            getString(com.despaircorp.shared.R.string.loft),
        )
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        binding.addEstatePopUpSpinnerEstateType.adapter = spinnerAdapter
    }
    
    private fun initSoldAndForSaleButton() {
        binding.addEstatePopUpButtonForSale.setOnClickListener {
            viewModel.onForSaleButtonClicked()
            
        }
        
        binding.addEstatePopUpButtonForSold.setOnClickListener {
            viewModel.onSoldButtonClicked()
            
        }
    }
    
    private fun initEntryDatePicker() {
        binding.activityCreateEstateButtonEntryDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_date))
                    .build()
            
            datePicker.addOnPositiveButtonClickListener {
                val localDate =
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                
                binding.activityCreateEstateButtonEntryDate.text = localDate
                viewModel.onEntryDateChanged(localDate)
                
            }
            
            datePicker.show(supportFragmentManager, "entry_date");
        }
    }
    
    private fun initSoldDatePicker() {
        binding.activityCreateEstateButtonSoldDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_date))
                    .build()
            
            datePicker.addOnPositiveButtonClickListener {
                val localDate =
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                
                binding.activityCreateEstateButtonSoldDate.text = localDate
                viewModel.onSoldDateChanged(localDate)
            }
            
            datePicker.show(supportFragmentManager, "sold_date");
        }
    }
    
    private fun imagePickerInit() {
        binding.addEstatePopUpButtonAddPicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageResultLauncher.launch(intent)
        }
    }
    
    private fun takePictureInit() {
        binding.addEstatePopUpButtonTakePicture.setOnClickListener {
            
            currentPhotoUri = FileProvider.getUriForFile(
                this,
                "$packageName.provider",
                File.createTempFile(
                    "JPEG_",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
            )
            
            takePictureCallback.launch(currentPhotoUri)
        }
    }
    
    private fun showImageTypeDialog(imageUri: Uri) {
        val imageTypes = EstatePictureType.entries.toTypedArray()
        val items = imageTypes.map { it.name.capitalize(Locale.getDefault()) }.toTypedArray()
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Image Type")
            .setItems(items) { _, which ->
                val selectedType = imageTypes[which]
                
                viewModel.onAddPicture(selectedType, imageUri, which)
            }
            .show()
    }
    
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_CURRENT_PHOTO_URI, currentPhotoUri)
    }
    
    companion object {
        private const val KEY_CURRENT_PHOTO_URI = "KEY_CURRENT_PHOTO_URI"
        private const val ARG_IS_EDIT_MODE = "ARG_IS_EDIT_MODE"
        private const val ARG_TO_EDIT_ESTATE_ID = "ARG_TO_EDIT_ESTATE_ID"
        
        fun navigate(
            context: Context,
            isEditMode: Boolean,
            toEditEstateId: Int
        ) = Intent(
            context,
            EstateFormActivity::class.java
        ).apply {
            putExtra(ARG_IS_EDIT_MODE, isEditMode)
            putExtra(ARG_TO_EDIT_ESTATE_ID, toEditEstateId)
        }
    }
    
    override fun onAddPointOfInterestClicked(id: Int) {
        viewModel.onAddPointOfInterest(id)
    }
    
    override fun onRemovePointOfInterestClicked(id: Int) {
        viewModel.onRemovePointOfInterest(id)
    }
    
    override fun onAgentClick(agentId: Int) {
        viewModel.onAgentClicked(agentId)
    }
}