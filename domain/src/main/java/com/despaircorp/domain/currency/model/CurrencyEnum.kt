package com.despaircorp.domain.currency.model

import com.despaircorp.shared.R

enum class CurrencyEnum(val symbolResource: Int, val isTrailingSymbol: Boolean) {
    US_DOLLAR(R.string.us_dollar_symbol, true),
    EURO(R.string.euro_symbol, false)
}