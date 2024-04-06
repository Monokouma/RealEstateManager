package com.despaircorp.ui.main.activity.composable.pop_up.estate_addition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.despaircorp.shared.R
import com.despaircorp.ui.theme.merriweatherSans


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