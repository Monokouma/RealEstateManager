package com.despaircorp.ui.main.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.despaircorp.ui.login.activity.LoginActivity
import com.despaircorp.ui.main.MainState
import com.despaircorp.ui.main.MainViewModel
import com.despaircorp.ui.main.activity.composable.loading_screen.LoadingScreen
import com.despaircorp.ui.main.activity.composable.main_screen.MainScreen
import com.despaircorp.ui.theme.RealEstateManagerKotlinTheme
import dagger.hilt.android.AndroidEntryPoint


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
            
            MainState.Loading -> {
                LoadingScreen(modifier = modifier)
            }
            
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

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    RealEstateManagerKotlinTheme {
    
    }
}