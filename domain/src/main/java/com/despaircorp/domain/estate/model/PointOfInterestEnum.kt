package com.despaircorp.domain.estate.model

import com.despaircorp.shared.R

enum class PointOfInterestEnum(val textRes: Int, val id: Int) {
    SCHOOLS(R.string.schools, 0),
    PARK(R.string.parks, 1),
    SUBWAY(R.string.subway, 2),
    SHOP(R.string.shops, 3)
}