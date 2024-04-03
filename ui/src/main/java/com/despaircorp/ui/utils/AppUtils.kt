package com.despaircorp.ui.utils

import java.text.DecimalFormat

fun getEuroFromDollar(dollars: String): String {
    val dollarsInt = dollars.replace(".", "").toInt()
    val rate = 0.9307
    
    return DecimalFormat("#,###,###")
        .format((dollarsInt * rate))
        .replace(",", ".")
}