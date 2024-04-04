package com.despaircorp.ui.main.activity.composable.details.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.shared.R
import com.despaircorp.ui.main.activity.composable.details.EstateBathRoomNumber
import com.despaircorp.ui.main.activity.composable.details.EstateBedRoomNumber
import com.despaircorp.ui.main.activity.composable.details.EstateFullAddress
import com.despaircorp.ui.main.activity.composable.details.EstateRoomNumber
import com.despaircorp.ui.main.activity.composable.details.EstateSurfaceSize
import com.despaircorp.ui.main.activity.composable.details.ImageCarousel
import com.despaircorp.ui.main.activity.entity.EstateWithPictureViewEntity
import com.despaircorp.ui.theme.merriweatherSans
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun TabletEstateDetails(entity: EstateWithPictureViewEntity) {
    Card {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.media),
                fontSize = 24.sp,
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.size(16.dp))
            ImageCarousel(imageData = entity.estate?.pictures ?: emptyList())
            Spacer(Modifier.size(16.dp))
            Text(
                text = stringResource(id = R.string.description),
                fontSize = 24.sp,
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = entity.estate?.estateEntity?.description ?: "",
                fontSize = 16.sp,
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(Modifier.size(40.dp))
            
            Row(
                Modifier.fillMaxWidth(), // Fill the width of the parent
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                
                Column(Modifier.weight(1f)) {
                    EstateSurfaceSize(surface = entity.estate?.estateEntity?.surface ?: "")
                    Spacer(Modifier.size(8.dp))
                    EstateRoomNumber(entity.estate?.estateEntity?.roomNumber ?: 0)
                    Spacer(Modifier.size(8.dp))
                    EstateBathRoomNumber(entity.estate?.estateEntity?.bathroomNumber ?: 0)
                    Spacer(Modifier.size(8.dp))
                    EstateBedRoomNumber(entity.estate?.estateEntity?.numberOfBedrooms ?: 0)
                    Spacer(Modifier.size(8.dp))
                }
                Spacer(Modifier.size(16.dp))
                Column(Modifier.weight(1f)) {
                    EstateFullAddress(address = entity.estate?.estateEntity?.address ?: "")
                }
                Spacer(Modifier.size(16.dp))
                Column(Modifier.weight(1f)) {
                    MapPlaceForTablet(
                        entity.estate?.estateEntity?.location ?: LatLng(0.0, 0.0),
                        entity.estate?.estateEntity?.city ?: ""
                    )
                }
            }
        }
    }
}

@Composable
fun MapPlaceForTablet(estatePosition: LatLng, city: String) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = CameraPositionState(
            CameraPosition.fromLatLngZoom(
                estatePosition,
                18f
            )
        )
    ) {
        Marker(
            state = MarkerState(position = estatePosition),
            title = city,
        )
    }
}