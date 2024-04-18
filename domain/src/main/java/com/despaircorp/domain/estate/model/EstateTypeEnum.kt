package com.despaircorp.domain.estate.model

import com.despaircorp.shared.R

enum class EstateTypeEnum(val textRes: Int, val id: Int) {
    MANOR(R.string.manor, 0),
    LOFT(R.string.loft, 1),
    APARTMENT(R.string.apartment, 2),
    HOUSE(R.string.house, 3)
}