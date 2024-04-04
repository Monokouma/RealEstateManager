package com.despaircorp.ui.login.activity.composable.agent_drop_down

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.shared.R
import com.despaircorp.ui.theme.merriweatherSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateAgentDropDown(
    realEstateAgentEntities: List<RealEstateAgentEntity>,
    modifier: Modifier,
    onClick: (Int) -> Unit
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
                    onClick.invoke(selectedId)
                }
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                stringResource(R.string.continue_button), fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}