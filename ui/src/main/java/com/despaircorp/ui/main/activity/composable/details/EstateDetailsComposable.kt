package com.despaircorp.ui.main.activity.composable.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.domain.picture.model.EstatePicture
import com.despaircorp.shared.R
import com.despaircorp.ui.main.activity.composable.details.smartphone.SmartphoneEstateDetails
import com.despaircorp.ui.main.activity.composable.details.tablet.TabletEstateDetails
import com.despaircorp.ui.main.activity.entity.EstateWithPictureViewEntity
import com.despaircorp.ui.theme.merriweatherSans

@Composable
fun EstateDetails(entity: EstateWithPictureViewEntity, windowsSize: WindowWidthSizeClass) {
    if (windowsSize == WindowWidthSizeClass.Expanded) {
        TabletEstateDetails(entity = entity)
    } else {
        SmartphoneEstateDetails(entity)
    }
}


@Composable
fun EstateFullAddress(address: String) {
    Row() {
        Icon(
            painter = painterResource(id = R.drawable.maps_and_flags),
            contentDescription = "Surface ",
            tint = MaterialTheme.colorScheme.outline,
        )
        
        Spacer(Modifier.size(16.dp))
        
        Column {
            Text(text = "Location")
            Spacer(Modifier.size(4.dp))
            Text(text = address)
        }
    }
}

@Composable
fun EstateSurfaceSize(surface: String) {
    
    Row {
        Icon(
            painter = painterResource(id = R.drawable.area),
            contentDescription = "Surface ",
            tint = MaterialTheme.colorScheme.outline,
        )
        
        Spacer(Modifier.size(16.dp))
        
        Column {
            Text(text = "Surface")
            Spacer(Modifier.size(4.dp))
            Text(text = surface)
        }
    }
}

@Composable
fun EstateRoomNumber(numberOfRoom: Int) {
    
    Row {
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Number of rooms",
            tint = MaterialTheme.colorScheme.outline,
        )
        
        Spacer(Modifier.size(16.dp))
        
        Column {
            Text(text = "Number of rooms")
            Spacer(Modifier.size(4.dp))
            Text(text = numberOfRoom.toString())
        }
    }
}

@Composable
fun EstateBathRoomNumber(numberOfBathroom: Int) {
    
    Row {
        Icon(
            painter = painterResource(id = R.drawable.bathtub),
            contentDescription = "Number of bathrooms",
            tint = MaterialTheme.colorScheme.outline,
        )
        
        Spacer(Modifier.size(16.dp))
        
        Column {
            Text(text = "Number of bathrooms")
            Spacer(Modifier.size(4.dp))
            Text(text = numberOfBathroom.toString())
        }
    }
}

@Composable
fun EstateBedRoomNumber(bedRoomNumber: Int) {
    
    Row {
        Icon(
            painter = painterResource(id = R.drawable.bedroom),
            contentDescription = "Number of bedrooms",
            tint = MaterialTheme.colorScheme.outline,
        )
        
        Spacer(Modifier.size(16.dp))
        
        Column {
            Text(text = "Number of bedrooms")
            Spacer(Modifier.size(4.dp))
            Text(text = bedRoomNumber.toString())
        }
    }
}

@Composable
fun ImageCarousel(imageData: List<EstatePicture>) {
    
    LazyRow() {
        items(imageData.size) { index -> // Use the size of the list here
            val data = imageData[index] // Get the item from the list using the index
            
            Card(
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                ) {
                    Image(
                        bitmap = data.bitmapImage.asImageBitmap(),
                        contentDescription = data.description.name, // Accessibility description
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(140.dp)
                    
                    )
                    Text(
                        text = data.description.name.uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                        fontFamily = merriweatherSans,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color(0xAA000000)) // Semi-transparent black
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}