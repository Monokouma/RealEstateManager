package com.despaircorp.ui.main.activity.composable.pop_up.estate_addition

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.domain.picture.model.EstatePictureType
import com.despaircorp.ui.theme.RealEstateManagerKotlinTheme
import com.despaircorp.ui.theme.merriweatherSans
import com.despaircorp.ui.utils.uriToBitmap

@Composable
fun EstateCreationForm(
    modifier: Modifier,
    onCreatePropertyClick: (NewPropertyEntity) -> Unit
) {
    CreatePropertyScreen(
        onCreatePropertyClick
    )
}

@Composable
fun CreatePropertyScreen(
    onCreatePropertyClick: (NewPropertyEntity) -> Unit
) {
    // State variables to hold the new property values
    var surface by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var numberOfRooms by rememberSaveable { mutableStateOf("") }
    var numberOfBathrooms by rememberSaveable { mutableStateOf("") }
    var numberOfBedrooms by rememberSaveable { mutableStateOf("") }
    val estateImages = remember { mutableStateListOf<NewPictureEntity>() }
    
    
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .widthIn(600.dp, 600.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EstateCreationTitle()
        
        Spacer(Modifier.height(16.dp))
        
        EstateCreationInput(
            surface = surface,
            description = description,
            location = location,
            numberOfRooms = numberOfRooms,
            numberOfBathrooms = numberOfBathrooms,
            numberOfBedrooms = numberOfBedrooms,
            onSurfaceInputChanged = { surface = it },
            onDescriptionInputChanged = { description = it },
            onNumberOfRoomInputChanged = { numberOfRooms = it },
            onNumberOfBathroomsInputChanged = { numberOfBathrooms = it },
            onNumberOfBedroomsInputChanged = { numberOfBedrooms = it },
            onLocationInputChanged = { location = it }
        )
        
        Spacer(Modifier.height(8.dp))
        
        EstateCreationImagePicker(
            onImageAddition = { list ->
                estateImages.clear()
                estateImages.addAll(list)
            }
        )
        
        Spacer(Modifier.height(16.dp))
        
        EstateCreationValidateButton(
            onCreatePropertyClick = onCreatePropertyClick,
            surface = surface,
            description = description,
            location = location,
            numberOfRooms = numberOfRooms,
            numberOfBathrooms = numberOfBathrooms,
            numberOfBedrooms = numberOfBedrooms,
            estateImages = estateImages
        )
    }
}

@Composable
fun EstateCreationValidateButton(
    onCreatePropertyClick: (NewPropertyEntity) -> Unit,
    surface: String,
    description: String,
    location: String,
    numberOfRooms: String,
    numberOfBathrooms: String,
    numberOfBedrooms: String,
    estateImages: SnapshotStateList<NewPictureEntity>,
    
    ) {
    ElevatedButton(
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        onClick = {
            onCreatePropertyClick.invoke(
                NewPropertyEntity(
                    description = description,
                    surface = surface,
                    location = location,
                    numberOfRooms = numberOfRooms,
                    numberOfBathrooms = numberOfBathrooms,
                    numberOfBedrooms = numberOfBedrooms,
                    picture = estateImages.toList()
                )
            )
        },
        enabled = surface.isNotEmpty() && location.isNotEmpty() && numberOfRooms.isNotEmpty()
                && numberOfBathrooms.isNotEmpty() && numberOfBedrooms.isNotEmpty() && description.isNotEmpty() && estateImages.isNotEmpty()
    ) {
        Text(
            "Create Property",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontFamily = merriweatherSans,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun EstateCreationInput(
    surface: String,
    description: String,
    location: String,
    numberOfRooms: String,
    numberOfBathrooms: String,
    numberOfBedrooms: String,
    onSurfaceInputChanged: (String) -> Unit,
    onDescriptionInputChanged: (String) -> Unit,
    onNumberOfRoomInputChanged: (String) -> Unit,
    onNumberOfBathroomsInputChanged: (String) -> Unit,
    onNumberOfBedroomsInputChanged: (String) -> Unit,
    onLocationInputChanged: (String) -> Unit,
) {
    
    Row(
        modifier = Modifier.widthIn(300.dp, 600.dp)
    ) {
        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionInputChanged.invoke(it) },
            label = { Text("Description") },
            placeholder = { Text("Enter a description") },
            modifier = Modifier.weight(1f)
        )
        
        Spacer(Modifier.width(8.dp))
        
        OutlinedTextField(
            value = surface,
            onValueChange = { onSurfaceInputChanged.invoke(it) },
            label = { Text("Surface") },
            placeholder = { Text("Enter surface e.g. 110m²") },
            modifier = Modifier.weight(1f)
        )
    }
    
    Spacer(Modifier.height(8.dp))
    
    
    Row(
        modifier = Modifier.widthIn(300.dp, 600.dp)
    ) {
        OutlinedTextField(
            value = location,
            onValueChange = { onLocationInputChanged.invoke(it) },
            label = { Text("Location") },
            placeholder = { Text("Enter address") },
            modifier = Modifier.weight(1f)
        )
        
        Spacer(Modifier.width(8.dp))
        
        OutlinedTextField(
            value = numberOfRooms,
            onValueChange = { onNumberOfRoomInputChanged.invoke(it) },
            label = { Text("Number of Rooms") },
            placeholder = { Text("Enter number of rooms") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
    
    Spacer(Modifier.height(8.dp))
    
    Row(
        modifier = Modifier.widthIn(300.dp, 600.dp)
    ) {
        OutlinedTextField(
            value = numberOfBathrooms,
            onValueChange = { onNumberOfBathroomsInputChanged.invoke(it) },
            label = { Text("Number of Bathrooms") },
            placeholder = { Text("Enter number of bathrooms") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        
        Spacer(Modifier.width(8.dp))
        
        OutlinedTextField(
            value = numberOfBedrooms,
            onValueChange = { onNumberOfBedroomsInputChanged.invoke(it) },
            label = { Text("Number of Bedrooms") },
            placeholder = { Text("Enter number of bedrooms") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EstateCreationTitle() {
    Text(
        "Create New Property",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = merriweatherSans,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
fun EstateCreationImagePicker(
    onImageAddition: (List<NewPictureEntity>) -> Unit
) {
    // A state list to hold the EstateImage data
    val estateImages = remember { mutableStateListOf<NewPictureEntity>() }
    
    // A state to keep track of if the dialog should be shown
    val showDialog = rememberSaveable { mutableStateOf(false) }
    
    // A state to keep track of the currently selected bitmap
    val currentBitmap = rememberSaveable { mutableStateOf<Bitmap?>(null) }
    val activity = (LocalContext.current as? Activity)
    
    // The launcher to pick the image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = uriToBitmap(activity?.applicationContext ?: return@let, it)
            bitmap?.let { bmp ->
                currentBitmap.value = bmp
                showDialog.value = true // Show the dialog after an image is selected
            }
        }
    }
    
    ElevatedButton(
        onClick = { imagePickerLauncher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            "Pick an image",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontFamily = merriweatherSans,
            color = MaterialTheme.colorScheme.outline
        )
    }
    
    Column(modifier = Modifier.padding(16.dp)) {
        
        if (estateImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            ImageCarousel(estateImages, onPictureClicked = {
                imagePickerLauncher.launch("image/*")
                estateImages.removeAt(it)
            })
        }
    }
    
    // Show the dialog when required
    if (showDialog.value) {
        ConfirmationDialog { description ->
            currentBitmap.value?.let { bmp ->
                estateImages.add(NewPictureEntity(bmp, description))
                showDialog.value = false // Hide the dialog once the user confirms
                onImageAddition.invoke(estateImages)
            }
        }
    }
}

// Function to handle URI to Bitmap conversion


@Composable
fun ImageCarousel(estateImages: List<NewPictureEntity>, onPictureClicked: (Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(estateImages.size) { index -> // Use the size of the list here
            val data = estateImages[index] // Get the item from the list using the index
            
            Card(
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                ) {
                    Image(
                        bitmap = data.imageBitmap.asImageBitmap(),
                        contentDescription = data.type.name, // Accessibility description
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .width(140.dp)
                            .clickable {
                                onPictureClicked.invoke(index)
                            }
                    )
                    
                    Text(
                        text = data.type.name.uppercase(),
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

@Composable
fun ConfirmationDialog(onConfirm: (EstatePictureType) -> Unit) {
    val options = EstatePictureType.entries
    var selectedOption by remember { mutableStateOf(options.first()) }
    
    AlertDialog(
        onDismissRequest = {},
        
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text("What is the image for?") },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (option == selectedOption),
                                onClick = { selectedOption = option }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { selectedOption = option }
                        )
                        
                        Text(
                            text = option.name,
                            modifier = Modifier.padding(start = 8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            fontFamily = merriweatherSans,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        },
        confirmButton = {
            ElevatedButton(
                onClick = { onConfirm(selectedOption) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    "Confirm",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontFamily = merriweatherSans,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}

data class NewPropertyEntity(
    val description: String,
    val surface: String,
    val location: String,
    val numberOfRooms: String,
    val numberOfBathrooms: String,
    val numberOfBedrooms: String,
    val picture: List<NewPictureEntity>
)

data class NewPictureEntity(
    val imageBitmap: Bitmap,
    val type: EstatePictureType
)

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    RealEstateManagerKotlinTheme {
        CreatePropertyScreen {
        
        }
    }
}
