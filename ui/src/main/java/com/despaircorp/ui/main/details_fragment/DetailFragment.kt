package com.despaircorp.ui.main.details_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.FragmentDetailsBinding
import com.despaircorp.ui.utils.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_details), OnMapReadyCallback {
    private val binding by viewBinding { FragmentDetailsBinding.bind(it) }
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mapView: MapView
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(
            requireActivity(),
            R.id.activity_main_FragmentContainer_view
        )
        if (activity?.resources?.getBoolean(R.bool.isLandscape) == true) {
            navController.navigateUp()
        }
        
        mapView = binding.fragmentDetailsMapMap
        
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }
    
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    
    override fun onResume() {
        super.onResume()
        // see bools.xml resource file
        // used to easily find device's orientation
        mapView.onResume()
        
        
    }
    
    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }
    
    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
    
    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    
    override fun onMapReady(p0: GoogleMap) {
        val itemId = arguments?.let {
            DetailFragmentArgs.fromBundle(it).estateId
        }
        
        p0.mapType = GoogleMap.MAP_TYPE_NORMAL
        p0.isBuildingsEnabled = false
        p0.uiSettings.isMapToolbarEnabled = true
        viewModel.onReceiveEstateId(itemId ?: 1)
        val pictureAdapter = EstatePictureAdapter()
        binding.fragmentDetailsRecyclerViewPicture.adapter = pictureAdapter
        
        viewModel.viewState.observe(viewLifecycleOwner) {
            binding.fragmentDetailsTextViewDescription.text = it.description
            binding.fragmentDetailsTextViewSurface.text = it.surface
            binding.fragmentDetailsTextViewRoom.text = "${it.roomNumber}"
            binding.fragmentDetailsTextViewBathrooms.text = "${it.bathroomNumber}"
            binding.fragmentDetailsTextViewBedrooms.text = "${it.bedroomNumber}"
            binding.fragmentDetailsTextViewLocation.text = it.address
            binding.fragmentDetailsMapMap.isVisible = it.willShowMap
            binding.fragmentDetailsTextViewNotAvailableMap.isVisible =
                it.willShowUnavailableMapMessage
            pictureAdapter.submitList(it.pictureViewStateItems)
            
            val cameraPosition = CameraPosition.Builder()
                .target(it.latLng)
                .zoom(18f)
                .build()
            
            val markerOptions = MarkerOptions()
                .position(it.latLng)
                .title(it.address)
            
            p0.addMarker(markerOptions)
            p0.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}