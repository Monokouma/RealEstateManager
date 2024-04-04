package com.despaircorp.ui.main.activity.composable.top_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.despaircorp.shared.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onClickDrawer: () -> Unit,
    onClickIcon: () -> Unit
) {
    
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.outline,
        ),
        title = {
            Text(
                stringResource(id = R.string.app_name_formated),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onClickDrawer.invoke()
                
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                
                onClickIcon.invoke()
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
}