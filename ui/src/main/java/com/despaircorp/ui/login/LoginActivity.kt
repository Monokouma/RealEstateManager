package com.despaircorp.ui.login

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.R
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
}

@Composable
fun LoginMain(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val isSplashScreenShown by viewModel.isSplashScreenShownLiveData.observeAsState(initial = false)
    val realEstateAgentEntities by viewModel.realEstateAgentEntitiesLiveDate.observeAsState(
        emptyList()
    )
    
    LaunchedEffect(Unit) {
        viewModel.startSplashScreenTime()
        viewModel.fetchRealEstateAgentList()
    }
    
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        if (isSplashScreenShown) {
            //Show Login
            Login(modifier, realEstateAgentEntities)
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
fun Login(modifier: Modifier = Modifier, realEstateAgentEntities: List<RealEstateAgentEntity>) {
    
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
        
        RealEstateAgentDropDown(realEstateAgentEntities, modifier)
    }
}

@Composable
fun RealEstateAgentDropDown(
    realEstateAgentEntities: List<RealEstateAgentEntity>,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    var chosenName by remember { mutableStateOf("Chose Someone") }
    
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(onClick = { expanded = true }) {
            Text(text = chosenName)
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            realEstateAgentEntities.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = item.imageResource),
                                contentDescription = ""
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
                        chosenName = item.name
                    }
                )
            }
        }
        
        // Text("Selected item: ${realEstateAgentEntities[selectedIndex].name}")
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    RealEstateManagerKotlinTheme {
        RealEstateAgentDropDown(realEstateAgentEntities = emptyList(), Modifier)
    }
}