package com.despaircorp.ui.main.activity.composable.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material.icons.sharp.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.shared.R
import com.despaircorp.ui.theme.merriweatherSans

@Composable
fun DrawerModal(
    modifier: Modifier,
    realEstateAgentEntity: RealEstateAgentEntity,
    onClick: (Int) -> Unit,
    onCurrencyClick: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val items = listOf(Icons.Sharp.ExitToApp)
    
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
                icon = {
                    Icon(Icons.Sharp.ShoppingCart, null)
                },
                label = {
                    Text(
                        stringResource(id = R.string.change_currency),
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                selected = item == selectedItem.value,
                onClick = {
                    selectedItem.value = item
                    onCurrencyClick.invoke()
                    onCloseDrawer.invoke()
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                    .padding(vertical = 16.dp)
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Sharp.ExitToApp, null) },
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