package com.despaircorp.ui.main.activity.composable.screen_holder

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.main.activity.composable.drawer.DrawerModal
import com.despaircorp.ui.main.activity.composable.screen_content.ScreenContent
import com.despaircorp.ui.main.activity.composable.top_bar.MainTopBar
import kotlinx.coroutines.launch

@Composable
fun ScreenHolder(
    realEstateAgentEntity: RealEstateAgentEntity,
    modifier: Modifier,
    onClick: (Int) -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    onCurrencyClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    
    var isAddPopUpVisibleActivityCallback by rememberSaveable {
        mutableStateOf(false)
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerModal(
                modifier = modifier,
                realEstateAgentEntity = realEstateAgentEntity,
                onClick = onClick,
                onCurrencyClick = onCurrencyClick,
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    MainTopBar(
                        onClickDrawer = {
                            scope.launch { drawerState.open() }
                        },
                        onClickIcon = {
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        }
                    )
                },
                content = { innerPadding ->
                    ScreenContent(
                        modifier = modifier,
                        innerPadding = innerPadding,
                        isAddPopUpVisibleActivityCallback = isAddPopUpVisibleActivityCallback,
                        onNavigationIconClicked = {
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        }, onValueAgentNameTextChange = onValueAgentNameTextChange,
                        onCreateAgentClick = {
                            onCreateAgentClick.invoke()
                            isAddPopUpVisibleActivityCallback =
                                !isAddPopUpVisibleActivityCallback
                        },
                        estateWithPictureEntities
                    )
                },
            )
        }
    )
}
