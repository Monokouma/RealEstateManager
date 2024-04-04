package com.despaircorp.ui.main.activity.composable.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.despaircorp.domain.estate.model.EstateWithPictureEntity
import com.despaircorp.domain.real_estate_agent.model.RealEstateAgentEntity
import com.despaircorp.ui.main.activity.composable.screen_holder.ScreenHolder


@Composable
fun MainScreen(
    realEstateAgentEntity: RealEstateAgentEntity,
    modifier: Modifier,
    onClick: (Int) -> Unit,
    onValueAgentNameTextChange: (String) -> Unit,
    onCreateAgentClick: () -> Unit,
    estateWithPictureEntities: List<EstateWithPictureEntity>,
    onCurrencyClick: () -> Unit
) {
    Column {
        ScreenHolder(
            realEstateAgentEntity = realEstateAgentEntity,
            modifier = modifier,
            onClick = onClick,
            onValueAgentNameTextChange = onValueAgentNameTextChange,
            onCreateAgentClick = onCreateAgentClick,
            estateWithPictureEntities,
            onCurrencyClick
        )
    }
}