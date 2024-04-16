package com.despaircorp.ui.main.details_fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.despaircorp.ui.R
import com.despaircorp.ui.databinding.FragmentDetailsBinding
import com.despaircorp.ui.main.details_fragment.picture.EstatePictureAdapter
import com.despaircorp.ui.main.main_activity.utils.viewBinding
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
    private lateinit var mapView: MapView
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val itemId = requireArguments().getInt(ARG_ITEM_ID)
            viewModel.onReceiveEstateId(itemId)
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
        p0.mapType = GoogleMap.MAP_TYPE_NORMAL
        p0.uiSettings.isMapToolbarEnabled = true
        
        val pictureAdapter = EstatePictureAdapter()
        binding.fragmentDetailsRecyclerViewPicture.adapter = pictureAdapter
        
        viewModel.viewState.observe(viewLifecycleOwner) {
            binding.fragmentDetailsTextViewDescription.text = it.description
            
            binding.fragmentDetailsConstraintLayoutSurfaceRoot.textTextView.text = it.surface
            binding.fragmentDetailsConstraintLayoutRoomRoot.textTextView.text = "${it.roomNumber}"
            binding.fragmentDetailsConstraintLayoutBathroomsRoot.textTextView.text =
                "${it.bathroomNumber}"
            binding.fragmentDetailsConstraintLayoutBedroomsRoot.textTextView.text =
                "${it.bedroomNumber}"
            binding.fragmentDetailsConstraintLayoutLocationRoot.textTextView.text = it.address
            
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
    
    companion object {
        private const val ARG_ITEM_ID = "ARG_ITEM_ID"
        
        fun newInstance(itemId: Int): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = args
            return fragment
        }
    }
    
}