package com.despaircorp.ui.main.activity.composable.screen_content

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.ui.main.activity.composable.details.EstateDetails
import com.despaircorp.ui.main.activity.composable.list.EstateList
import com.despaircorp.ui.main.activity.composable.pop_up.PopupFormDialog
import com.despaircorp.ui.main.activity.entity.EstateWithPictureViewEntity

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ScreenContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    isAddPopUpVisibleActivityCallback: Boolean,
    onNavigationIconClicked: () -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>
) {
    val activity = (LocalContext.current as? Activity)
    val windowsSize = calculateWindowSizeClass(activity = activity ?: return).widthSizeClass
    
    Box(
        modifier = modifier
            .padding(top = innerPadding.calculateTopPadding())
    ) {
        
        var selectedItem: EstateWithPictureViewEntity? by rememberSaveable(stateSaver = EstateWithPictureViewEntity.Saver) {
            mutableStateOf(EstateWithPictureViewEntity(0, estateWithPictureEntities.first()))
        }

        val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
        
        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
        
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane(modifier) {
                    EstateList(
                        onItemClick = { id ->
                            // Set current item
                            selectedItem = id
                            // Display the detail pane
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        },
                        estateWithPictureEntities,
                        modifier,
                        windowsSize
                    )
                }
            },
            detailPane = {
                AnimatedPane(Modifier.background(MaterialTheme.colorScheme.background)) {
                    // Show the detail pane content if selected item is available
                    selectedItem?.let { item ->
                        EstateDetails(item, windowsSize)
                    }
                }
            },
        )
        
        if (isAddPopUpVisibleActivityCallback) {
            PopupFormDialog(onSurfaceClicked = {
                onNavigationIconClicked.invoke()
                
            }, modifier, onValueAgentNameTextChange, onCreateAgentClick)
            
        }
    }
}