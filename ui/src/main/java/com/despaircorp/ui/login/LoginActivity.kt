package com.despaircorp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.R
import com.despaircorp.ui.main.MainActivity
import com.despaircorp.ui.theme.RealEstateManagerKotlinTheme
import com.despaircorp.ui.theme.merriweatherSans
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            RealEstateManagerKotlinTheme {
                // A surface container using the 'background' color from the theme
                LoginMain(
                    modifier = Modifier,
                    viewModel
                )
            }
        }
        
    }
    
    companion object {
        
        fun navigate(context: Context) = Intent(
            context,
            LoginActivity::class.java
        )
    }
}

@Composable
fun LoginMain(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val isSplashScreenShown by viewModel.isSplashScreenShownLiveData.observeAsState(initial = false)
    val realEstateAgentEntities by viewModel.realEstateAgentEntitiesLiveDate.observeAsState(
        emptyList()
    )
    val isAgentCurrentlyLoggedIn by viewModel.isAgentCurrentlyLoggedInLiveData.observeAsState(false)
    
    LaunchedEffect(Unit) {
        viewModel.startSplashScreenTime()
        viewModel.fetchRealEstateAgentList()
        viewModel.isAgentAlreadyLoggedIn()
    }
    
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        val activity = (LocalContext.current as? Activity)
        
        if (isSplashScreenShown) {
            //Show Login
            if (isAgentCurrentlyLoggedIn) {
                activity?.startActivity(MainActivity.navigate(activity.applicationContext))
                activity?.finish()
            } else {
                Login(modifier, realEstateAgentEntities, viewModel)
            }
        } else {
            //Show Splash Screen
            SplashScreen(modifier)
        }
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painter = painterResource(id = R.drawable.house), contentDescription = "")
        
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        
        LinearProgressIndicator(
            modifier = Modifier
                .padding(vertical = 20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            trackColor = MaterialTheme.colorScheme.primary,
        )
        
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = modifier.padding(vertical = 20.dp),
            fontFamily = merriweatherSans,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun Login(
    modifier: Modifier = Modifier,
    realEstateAgentEntities: List<RealEstateAgentEntity>,
    viewModel: LoginViewModel
) {
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painter = painterResource(id = R.drawable.house), contentDescription = "")
        
        Text(
            text = stringResource(id = R.string.login_greetings),
            modifier = modifier
                .padding(vertical = 20.dp),
            fontFamily = merriweatherSans,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.outline
        )
        
        RealEstateAgentDropDown(realEstateAgentEntities, modifier, viewModel)
        
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateAgentDropDown(
    realEstateAgentEntities: List<RealEstateAgentEntity>,
    modifier: Modifier,
    viewModel: LoginViewModel,
    
    ) {
    val text = stringResource(R.string.chose_someone)
    var isExpanded by remember {
        mutableStateOf(false)
    }
    
    var agentChosen by remember {
        mutableStateOf(text)
    }
    
    var selectedIcon by remember {
        mutableStateOf(R.drawable.gamer)
    }
    
    var selectedIndex by remember { mutableStateOf(0) }
    var selectedId by remember { mutableStateOf(0) }
    
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val activity = (LocalContext.current as? Activity)
        
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = it
            }
        ) {
            TextField(
                value = agentChosen,
                onValueChange = { },
                readOnly = true,
                leadingIcon = {
                    Image(painter = painterResource(id = selectedIcon), contentDescription = "")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = modifier
                    .menuAnchor()
                    .focusProperties {
                        canFocus = false
                    },
                
                )
            
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
                realEstateAgentEntities.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = item.imageResource),
                                    contentDescription = "",
                                    modifier = modifier.padding(8.dp)
                                )
                                Text(
                                    text = item.name,
                                    modifier = modifier.padding(8.dp),
                                    fontFamily = merriweatherSans,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            
                        },
                        onClick = {
                            selectedIndex = index
                            isExpanded = false
                            selectedIcon = item.imageResource
                            agentChosen = item.name
                            selectedId = item.id
                        }
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(vertical = 24.dp),
            onClick = {
                
                if (selectedId == 0) {
                    Toast.makeText(
                        activity?.applicationContext,
                        activity?.applicationContext?.getString(R.string.you_have_to_chose_a_profile_to_continue),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.onSelectedAgent(selectedId)
                    activity?.startActivity(MainActivity.navigate(activity.applicationContext))
                    activity?.finish()
                }
            }
        ) {
            Text(stringResource(R.string.continue_button))
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    RealEstateManagerKotlinTheme {
        //Login(realEstateAgentEntities = emptyList(), viewModel = viewModel)
    }
}