package com.devsan.santiniketanmess.models

data class Expense(
    val totalMarketing: Double = 0.0,
    val rice: Double = 0.0,
    val gas: Double = 0.0,
    val cookCharge: Double = 0.0,
    val paperCharge: Double = 0.0,
    val otherCharge: Double = 0.0,
    val guestMeal: Double = 0.0,
    val month: String = ""
)
