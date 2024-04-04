package com.despaircorp.ui.main.activity.composable.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.ui.main.activity.entity.EstateWithPictureViewEntity
import com.despaircorp.ui.theme.merriweatherSans

@Composable
fun EstateList(
    onItemClick: (EstateWithPictureViewEntity) -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    modifier: Modifier,
    windowsSize: WindowWidthSizeClass
) {
    
    
    var selectedItem: EstateWithPictureViewEntity? by rememberSaveable(stateSaver = EstateWithPictureViewEntity.Saver) {
        mutableStateOf(EstateWithPictureViewEntity(0, estateWithPictureEntities.first()))
    }
    
    Card {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            estateWithPictureEntities.forEachIndexed { id, item ->
                item {
                    val isSelected = selectedItem?.id == id
                    
                    ListItem(
                        modifier = modifier
                            .clickable {
                                selectedItem = EstateWithPictureViewEntity(id, item)
                                onItemClick(EstateWithPictureViewEntity(id, item))
                            },
                        
                        headlineContent = {
                            Row(
                            
                            ) {
                                Image(
                                    bitmap = item.pictures.first().bitmapImage.asImageBitmap(),
                                    contentDescription = item.pictures.first().description.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .width(140.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                )
                                
                                Column(
                                    modifier.padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = item.estateEntity.estateType,
                                        fontFamily = merriweatherSans,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    
                                    Text(
                                        text = item.estateEntity.city,
                                        fontFamily = merriweatherSans,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    
                                    Text(
                                        text = item.estateEntity.price,
                                        fontFamily = merriweatherSans,
                                        fontWeight = FontWeight.Normal,
                                        color = if (windowsSize == WindowWidthSizeClass.Compact || windowsSize == WindowWidthSizeClass.Medium) {
                                            MaterialTheme.colorScheme.tertiary
                                        } else {
                                            if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.tertiary
                                        }
                                    )
                                }
                            }
                        }, colors = ListItemDefaults.colors(
                            containerColor = if (windowsSize == WindowWidthSizeClass.Compact || windowsSize == WindowWidthSizeClass.Medium) {
                                MaterialTheme.colorScheme.background
                            } else {
                                if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background
                            }
                        )
                    )
                }
            }
        }
    }
}