package com.despaircorp.ui.main.activity.composable.pop_up

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.shared.R
import com.despaircorp.ui.main.activity.composable.form.AgentCreationForm
import com.despaircorp.ui.theme.merriweatherSans

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