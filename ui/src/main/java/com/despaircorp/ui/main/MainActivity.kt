package com.despaircorp.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.R
import com.despaircorp.ui.login.LoginActivity
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
                    Greeting(viewModel = viewModel)
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
fun Greeting(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val loggedInRealEstateAgentEntity =
        viewModel.loggedRealEstateAgentEntityLiveData.observeAsState()
    val isUserDisconnected = viewModel.isUserDisconnectedLiveData.observeAsState(false)
    val activity = (LocalContext.current as? Activity)
    
    LaunchedEffect(Unit) {
        viewModel.fetchLoggedRealEstateAgentEntity()
    }
    
    if (isUserDisconnected.value) {
        activity?.startActivity(LoginActivity.navigate(activity.applicationContext))
        activity?.finish()
    }
    
    Column {
        SimpleTopAppBar(loggedInRealEstateAgentEntity.value ?: return@Column, modifier, viewModel)
        
    }
}

@Composable
fun PopupFormDialog(
    onSurfaceClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {
                onSurfaceClicked.invoke()
            }
            .background(Color.Black.copy(alpha = 0.8f)) // Semi-transparent black
    
    ) {
        // Place your content here
        // Example: A text composable
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "LOL", Modifier.padding(20.dp))
                    
                }
            }
            
        }
        
        
        // You can add more composables here as needed
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(
    realEstateAgentEntity: RealEstateAgentEntity,
    modifier: Modifier,
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(Icons.Filled.ExitToApp)
    var isAddPopUpVisibleActivityCallback by remember {
        mutableStateOf(false)
    }
    
    val selectedItem = remember { mutableStateOf(items[0]) }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
                        label = { Text(stringResource(id = R.string.logout)) },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            viewModel.onDisconnect(realEstateAgentEntity.id)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
            
            
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.outline,
                        ),
                        title = {
                            Text(
                                stringResource(id = R.string.app_name_formated),
                                
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isAddPopUpVisibleActivityCallback =
                                    !isAddPopUpVisibleActivityCallback
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
                },
                content = { innerPadding ->
                    Box(
                        modifier = modifier.padding(innerPadding),
                    ) {
                        Column {
                            Text(text = "TEST")
                        }
                        if (isAddPopUpVisibleActivityCallback) {
                            PopupFormDialog(onSurfaceClicked = {
                                isAddPopUpVisibleActivityCallback =
                                    !isAddPopUpVisibleActivityCallback
                            })
                            
                        }
                        
                        
                    }
                    
                },
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    RealEstateManagerKotlinTheme {
        PopupFormDialog({})
    }
}
