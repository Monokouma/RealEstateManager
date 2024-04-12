package com.despaircorp.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.despaircorp.shared.R
import com.despaircorp.ui.databinding.ActivityMapBinding
import com.despaircorp.ui.main.details_fragment.DetailFragment
import com.despaircorp.ui.utils.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by viewBinding { ActivityMapBinding.inflate(it) }
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView
    private var isCameraPlaced = false
    private val alreadyPresentMarkersForEstateIds = mutableSetOf<Int>()
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        setSupportActionBar(binding.activityMapToolbarMain)
        
        binding.activityMapToolbarMain.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                binding.activityMapFragmentLayoutDetails.isVisible = false
                binding.activityMapMapView.isVisible = true
            } else {
                finish()
            }
            
        }
        
        mapView = binding.activityMapMapView
        
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        
        savedInstanceState?.getBoolean(ARGS_IS_CAMERA_PLACED).let {
            isCameraPlaced = it ?: false
        }
        
        savedInstanceState?.getIntegerArrayList(ARGS_MARKER_ID)?.let { list ->
            list.forEach { alreadyPresentMarkersForEstateIds.add(it) }
        }
    }
    
    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        p0.mapType = GoogleMap.MAP_TYPE_NORMAL
        p0.uiSettings.isMapToolbarEnabled = true
        p0.isMyLocationEnabled = true
        p0.uiSettings.isMapToolbarEnabled = true
        p0.uiSettings.isCompassEnabled = true
        p0.uiSettings.isMyLocationButtonEnabled = true
        p0.uiSettings.isZoomControlsEnabled = true
        
        
        
        viewModel.viewState.observe(this) {
            
            if (!it.isConnectedToInternet) {
                Toast.makeText(this, getString(R.string.required_internet), Toast.LENGTH_SHORT)
                    .show()
                finish()
            } else {
                
                it.mapViewStateItems.forEach { item ->
                    if (alreadyPresentMarkersForEstateIds.add(item.estateId)) {
                        p0.addMarker(
                            MarkerOptions()
                                .title(item.name)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        resizeBitmap(
                                            item.picturePath,
                                            40,
                                            40
                                        )
                                    )
                                )
                                .position(
                                    LatLng(
                                        item.latitude,
                                        item.longitude,
                                    )
                                )
                        )
                    }
                }
                
                p0.setOnMarkerClickListener { marker ->
                    val result = it.mapViewStateItems.find { item ->
                        item.name == marker.title
                    }
                    
                    if (result != null) {
                        binding.activityMapFragmentLayoutDetails.isVisible = true
                        binding.activityMapMapView.isVisible = false
                        val detailsFragment = DetailFragment.newInstance(result.estateId)
                        supportFragmentManager.beginTransaction()
                            .replace(
                                com.despaircorp.ui.R.id.activity_map_FragmentLayout_details,
                                detailsFragment
                            )
                            .addToBackStack("detail")
                            .commit()
                    }
                    true
                }
                
                if (!isCameraPlaced) {
                    isCameraPlaced = true
                    
                    p0.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            it.userLocation,
                            14f
                        )
                    )
                }
            }
        }
    }
    
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            binding.activityMapFragmentLayoutDetails.isVisible = false
            binding.activityMapMapView.isVisible = true
        } else {
            super.onBackPressed()
        }
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
        outState.putBoolean(ARGS_IS_CAMERA_PLACED, isCameraPlaced)
        outState.putIntegerArrayList(ARGS_MARKER_ID, ArrayList(alreadyPresentMarkersForEstateIds))
        
    }
    
    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    
    
    companion object {
        private const val ARGS_MARKER_ID = "ARGS_MARKER_ID"
        private const val ARGS_IS_CAMERA_PLACED = "ARGS_IS_CAMERA_PLACED"
        fun navigate(context: Context) = Intent(
            context,
            MapActivity::class.java
        )
    }
    
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
    
    private fun resizeBitmap(imagePath: String, newWidth: Int, newHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        
        BitmapFactory.decodeFile(imagePath, options)
        
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight)
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        
        return BitmapFactory.decodeFile(imagePath, options)
    }
    
}