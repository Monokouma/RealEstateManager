package com.despaircorp.ui.main.activity.composable.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.despaircorp.shared.R
import com.despaircorp.ui.main.activity.composable.text_input.TextFieldWithIcons
import com.despaircorp.ui.theme.merriweatherSans

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