package com.despaircorp.ui.main.estate_form.estate_type

import com.despaircorp.domain.estate.model.EstateTypeEnum

data class EstateTypeViewStateItems(
    val id: Int,
    val estateType: EstateTypeEnum,
    val isSelected: Boolean,
    val isFiltering: Boolean
)
