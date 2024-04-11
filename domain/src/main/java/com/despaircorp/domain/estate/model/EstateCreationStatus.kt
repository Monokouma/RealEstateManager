package com.despaircorp.domain.estate.model

import com.despaircorp.shared.R

enum class EstateCreationStatus(val message: Int) {
    EMPTY_SURFACE(R.string.surface_empty),
    EMPTY_DESCRIPTION(R.string.description_empty),
    EMPTY_ROOM_NUMBER(R.string.room_number_empty),
    EMPTY_BEDROOM_NUMBER(R.string.bedroom_number_empty),
    EMPTY_BATHROOM_NUMBER(R.string.bathroom_number_empty),
    EMPTY_ADDRESS(R.string.address_empty),
    EMPTY_CITY(R.string.city_empty),
    EMPTY_PRICE(R.string.price_empty),
    EMPTY_TYPE(R.string.type_empty),
    EMPTY_POINT_OF_INTEREST(R.string.point_of_interest_empty),
    EMPTY_ENTRY_DATE(R.string.entry_date_empty),
    EMPTY_SOLD_DATE(R.string.sold_date_empty),
    EMPTY_PICTURE(R.string.pictures_empty),
    SUCCESS(R.string.creation_success)
}