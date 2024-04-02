package com.despaircorp.ui.main.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.picture.model.EstatePicture
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.shared.R
import com.despaircorp.ui.login.activity.LoginActivity
import com.despaircorp.ui.main.MainState
import com.despaircorp.ui.main.MainViewModel
import com.despaircorp.ui.theme.RealEstateManagerKotlinTheme
import com.despaircorp.ui.theme.merriweatherSans
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            RealEstateManagerKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowsSize = calculateWindowSizeClass(activity = this).widthSizeClass
                    Main(viewModel = viewModel)
                    
                }
            }
        }
    }
    
    companion object {
        fun navigate(context: Context) = Intent(
            context,
            MainActivity::class.java
        )
    }
}

@Composable
fun Main(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val activity = (LocalContext.current as? Activity)
    
    Surface {
        when (val state = uiState.value) {
            MainState.Disconnected -> {
                activity?.startActivity(LoginActivity.navigate(activity.applicationContext))
                activity?.finish()
            }
            
            
            MainState.Loading -> {}
            
            is MainState.MainStateView -> {
                if (state.error.showError) {
                    Toast.makeText(
                        activity,
                        activity?.getString(state.error.errorMessageRes),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (state.onCreateAgentSuccess.showSuccess) {
                    Toast.makeText(
                        activity,
                        activity?.getString(state.onCreateAgentSuccess.successMessageRes),
                        Toast.LENGTH_SHORT
                    ).show()
                    
                }
                
                MainScreen(
                    realEstateAgentEntity = state.currentLoggedInAgent,
                    modifier = modifier,
                    onClick = { viewModel.onDisconnect(it) },
                    onValueAgentNameTextChange = { viewModel.onRealEstateAgentNameTextChange(it) },
                    onCreateAgentClick = { viewModel.onCreateAgentClick() },
                    state.estates,
                    onCurrencyClick = { viewModel.onChangeCurrencyClicked() }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    realEstateAgentEntity: RealEstateAgentEntity,
    modifier: Modifier,
    onClick: (Int) -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    onCurrencyClick: () -> Unit
) {
    Column {
        Holder(
            realEstateAgentEntity = realEstateAgentEntity,
            modifier = modifier,
            onClick = onClick,
            onValueAgentNameTextChange = onValueAgentNameTextChange,
            onCreateAgentClick = onCreateAgentClick,
            estateWithPictureEntities,
            onCurrencyClick
        )
    }
}

@Composable
fun Holder(
    realEstateAgentEntity: RealEstateAgentEntity,
    modifier: Modifier,
    onClick: (Int) -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    onCurrencyClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    
    var isAddPopUpVisibleActivityCallback by rememberSaveable {
        mutableStateOf(false)
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerModal(
                modifier = modifier,
                realEstateAgentEntity = realEstateAgentEntity,
                onClick = onClick,
                onCurrencyClick = onCurrencyClick,
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    MainTopBar(
                        onClickDrawer = {
                            scope.launch { drawerState.open() }
                        },
                        onClickIcon = {
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        }
                    )
                },
                content = { innerPadding ->
                    ScreenContent(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        isAddPopUpVisibleActivityCallback = isAddPopUpVisibleActivityCallback,
                        onNavigationIconClicked = {
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        }, onValueAgentNameTextChange = onValueAgentNameTextChange,
                        onCreateAgentClick = {
                            onCreateAgentClick.invoke()
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        },
                        estateWithPictureEntities
                    )
                },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ScreenContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    isAddPopUpVisibleActivityCallback: Boolean,
    onNavigationIconClicked: () -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>
) {
    Box(
        modifier = modifier
            .padding(top = innerPadding.calculateTopPadding())
    ) {
        
        var selectedItem: MyItem? by rememberSaveable(stateSaver = MyItem.Saver) {
            mutableStateOf(MyItem(0, estateWithPictureEntities.first()))
        }

// Create the ListDetailPaneScaffoldState
        val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
        
        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
        
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane(modifier) {
                    EstateList(
                        onItemClick = { id ->
                            // Set current item
                            selectedItem = id
                            // Display the detail pane
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        },
                        estateWithPictureEntities,
                        modifier
                    )
                }
            },
            detailPane = {
                AnimatedPane(Modifier.background(MaterialTheme.colorScheme.background)) {
                    // Show the detail pane content if selected item is available
                    selectedItem?.let { item ->
                        EstateDetails(item)
                    }
                }
            },
        )
        
        if (isAddPopUpVisibleActivityCallback) {
            PopupFormDialog(onSurfaceClicked = {
                onNavigationIconClicked.invoke()
                
            }, modifier, onValueAgentNameTextChange, onCreateAgentClick)
            
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun EstateList(
    onItemClick: (MyItem) -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    modifier: Modifier
) {
    val activity = (LocalContext.current as? Activity)
    val windowsSize = calculateWindowSizeClass(activity = activity ?: return).widthSizeClass
    
    var selectedItem: MyItem? by rememberSaveable(stateSaver = MyItem.Saver) {
        mutableStateOf(MyItem(0, estateWithPictureEntities.first()))
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
                                selectedItem = MyItem(id, item)
                                onItemClick(MyItem(id, item))
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

@Composable
fun EstateDetails(entity: MyItem) {
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
                    MapPlaceholder()
                }
            }
        }
    }
}

@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Gray), // Placeholder color, use actual map or image here
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Map Placeholder",
            color = Color.White
        )
        // Replace this Box with your actual Map composable or a static image of the map
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

class MyItem(val id: Int, val estate: EstateWithPictureEntity?) {
    companion object {
        val Saver: Saver<MyItem?, Bundle> = Saver(
            save = { bundleOf("id" to it?.id, "estate" to it?.estate?.estateEntity?.id) },
            restore = { MyItem(it.getInt("id"), it.getParcelable("estate")) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onClickDrawer: () -> Unit,
    onClickIcon: () -> Unit
) {
    
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.outline,
        ),
        title = {
            Text(
                stringResource(id = R.string.app_name_formated),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onClickDrawer.invoke()
                
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onClickIcon.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Localized description"
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
            
        },
    )
}

@Composable
fun DrawerModal(
    modifier: Modifier,
    realEstateAgentEntity: RealEstateAgentEntity,
    onClick: (Int) -> Unit,
    onCurrencyClick: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val items = listOf(Icons.Sharp.ExitToApp)
    
    val selectedItem = remember { mutableStateOf(items[0]) }
    
    ModalDrawerSheet {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(NavigationDrawerItemDefaults.ItemPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.drawer_house),
                contentDescription = ""
            )
        }
        Row(
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = realEstateAgentEntity.imageResource),
                contentDescription = "",
                modifier = modifier.padding(8.dp)
            )
            Text(
                text = realEstateAgentEntity.name,
                modifier = modifier.padding(8.dp),
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline
            )
        }
        
        Divider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(16.dp)
        )
        
        items.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(Icons.Sharp.ShoppingCart, null)
                },
                label = {
                    Text(
                        stringResource(id = R.string.change_currency),
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                selected = item == selectedItem.value,
                onClick = {
                    selectedItem.value = item
                    onCurrencyClick.invoke()
                    onCloseDrawer.invoke()
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                    .padding(vertical = 16.dp)
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Sharp.ExitToApp, null) },
                label = {
                    Text(
                        stringResource(id = R.string.logout),
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                selected = item == selectedItem.value,
                onClick = {
                    selectedItem.value = item
                    onClick.invoke(realEstateAgentEntity.id)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
fun PopupFormDialog(
    onSurfaceClicked: () -> Unit,
    modifier: Modifier,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
) {
    var isChoiceContentShown by rememberSaveable {
        mutableStateOf(true)
    }
    
    var isAddAgentFormIsShown by rememberSaveable {
        mutableStateOf(false)
    }
    
    var isAddEstateFormIsShown by rememberSaveable {
        mutableStateOf(false)
    }
    
    Box(
        modifier = Modifier
            .clickable {
                onSurfaceClicked.invoke()
            }
            .background(Color.Black.copy(alpha = 0.8f)) // Semi-transparent black
    
    ) {
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                if (isChoiceContentShown) {
                    ChoicePopUpContent(
                        modifier = modifier,
                        onAgentClick = {
                            isChoiceContentShown =
                                !isChoiceContentShown
                            isAddAgentFormIsShown = !isAddAgentFormIsShown
                        },
                        onEstateClick = {
                            isChoiceContentShown =
                                !isChoiceContentShown
                            isAddEstateFormIsShown = !isAddEstateFormIsShown
                        }
                    )
                } else {
                    if (isAddAgentFormIsShown) {
                        AgentCreationForm(
                            modifier = modifier,
                            onValueAgentNameTextChange,
                            onCreateAgentClick
                        )
                    }
                    
                    if (isAddEstateFormIsShown) {
                    
                    }
                }
                
            }
        }
    }
}

@Composable
fun AgentCreationForm(
    modifier: Modifier,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.agent_creation),
            modifier.padding(16.dp),
            fontFamily = merriweatherSans,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.outline
        )
        Column(
            modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldWithIcons(onValueAgentNameTextChange, modifier)
            ElevatedButton(
                modifier = Modifier
                    .padding(16.dp),
                onClick = {
                    onCreateAgentClick.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    "Create", fontFamily = merriweatherSans,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun TextFieldWithIcons(
    onValueAgentNameTextChange: (String) -> Unit,
    modifier: Modifier
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = "emailIcon") },
        onValueChange = {
            text = it
            onValueAgentNameTextChange.invoke(it.text)
        },
        label = { Text(text = "Name", color = MaterialTheme.colorScheme.outline) },
        placeholder = { Text(text = "Enter your name", color = MaterialTheme.colorScheme.outline) },
        
        )
}

@Composable
fun ChoicePopUpContent(
    modifier: Modifier,
    onAgentClick: () -> Unit,
    onEstateClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.what_you_wanna_add),
            Modifier.padding(20.dp),
            fontFamily = merriweatherSans,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.outline
        )
        
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.padding(bottom = 12.dp)
        ) {
            Spacer(modifier = modifier.padding(20.dp))
            PopUpAgentAdd(
                onClick = {
                    onAgentClick.invoke()
                },
                modifier
            )
            Spacer(modifier = modifier.padding(40.dp))
            PopUpEstateAdd(
                onClick = {
                    onEstateClick.invoke()
                },
                modifier
            )
            Spacer(modifier = modifier.padding(20.dp))
        }
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpEstateAdd(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        onClick = {
            onClick.invoke()
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.dream_home),
                contentDescription = "",
                modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.estate),
                modifier = modifier.padding(bottom = 8.dp),
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpAgentAdd(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        onClick = {
            onClick.invoke()
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.agent),
                contentDescription = "",
                modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = stringResource(R.string.agent),
                modifier = modifier.padding(bottom = 8.dp),
                fontFamily = merriweatherSans,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    RealEstateManagerKotlinTheme {
    
    }
}