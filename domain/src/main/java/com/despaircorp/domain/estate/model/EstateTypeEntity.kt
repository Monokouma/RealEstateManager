package com.despaircorp.domain.estate.model

data class EstateTypeEntity(
    val isSelected: Boolean,
    val estateTypeEnum: EstateTypeEnum,
    val id: Int,
)