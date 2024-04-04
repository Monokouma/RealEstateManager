package com.despaircorp.ui.login.activity.composable.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.shared.R
import com.despaircorp.ui.login.activity.composable.agent_drop_down.RealEstateAgentDropDown
import com.despaircorp.ui.theme.merriweatherSans


@Composable
fun Login(
    modifier: Modifier = Modifier,
    realEstateAgentEntities: List<RealEstateAgentEntity>,
    onClick: (Int) -> Unit
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
        
        RealEstateAgentDropDown(realEstateAgentEntities, modifier, onClick)
    }
}