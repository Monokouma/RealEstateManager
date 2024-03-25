package com.despaircorp.ui.main.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            RealEstateManagerKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isTablet = isTablet()
                    Log.i("Monokouma", isTablet.toString())
                    if (isTablet) {
                        MainTablet(viewModel = viewModel)
                    } else {
                        MainSmartphone(viewModel = viewModel)
                    }
                    
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
fun MainSmartphone(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    
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
                    state.estates
                )
            }
        }
    }
}

@Composable
fun MainTablet(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    
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
                    state.estates
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
    estateWithPictureEntities: List<EstateWithPictureEntity>
) {
    Column {
        Holder(
            realEstateAgentEntity = realEstateAgentEntity,
            modifier = modifier,
            onClick = onClick,
            onValueAgentNameTextChange = onValueAgentNameTextChange,
            onCreateAgentClick = onCreateAgentClick,
            estateWithPictureEntities
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
    estateWithPictureEntities: List<EstateWithPictureEntity>
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
                onClick = onClick
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
        modifier = modifier.padding(innerPadding),
    ) {
        
        var selectedItem: MyItem? by rememberSaveable(stateSaver = MyItem.Saver) {
            mutableStateOf(null)
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
                AnimatedPane(Modifier) {
                    MyList(
                        onItemClick = { id ->
                            // Set current item
                            selectedItem = id
                            // Display the detail pane
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        },
                        estateWithPictureEntities
                    )
                }
            },
            detailPane = {
                AnimatedPane(Modifier) {
                    // Show the detail pane content if selected item is available
                    selectedItem?.let { item ->
                        MyDetails(item)
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

@Composable
fun MyList(
    onItemClick: (MyItem) -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>
) {
    Card {
        LazyColumn {
            estateWithPictureEntities.forEachIndexed { id, item ->
                item {
                    ListItem(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .clickable {
                                onItemClick(MyItem(id))
                            },
                        headlineContent = {
                            Text(
                                text = item.estateEntity.estateType,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MyDetails(item: MyItem) {
    val text = "${item.id}"
    Card {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page for $text",
                fontSize = 24.sp,
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}

class MyItem(val id: Int) {
    companion object {
        val Saver: Saver<MyItem?, Int> = Saver(
            { it?.id },
            ::MyItem,
        )
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        configuration.screenWidthDp > 840
    } else {
        configuration.screenWidthDp > 600
    }
}

@Composable
fun EstateItem(
    modifier: Modifier,
    pictureBitmap: Bitmap,
    estateType: String,
    location: String,
    price: String
) {

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
    onClick: (Int) -> Unit
) {
    val items = listOf(Icons.Filled.ExitToApp)
    
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
                icon = { Icon(item, null) },
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