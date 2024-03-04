package com.despaircorp.data.real_estate_agent.workers

import com.despaircorp.shared.R


enum class DefaultAgentEnum(
    val displayNameRes: String,
    val id: Int,
    val imageRes: Int,
    val defaultIsLoggedIn: Boolean,
) {
    DARIUS("Darius", 1, R.drawable.darius, false),
    CASSIO("Cassio", 2, R.drawable.cassio, false),
    LUCIAN("Lucian", 3, R.drawable.lucian, false),
    BARD("Bard", 4, R.drawable.bard, false),
    NILAH("Nilah", 5, R.drawable.nilah, false),
}