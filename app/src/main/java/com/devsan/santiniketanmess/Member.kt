package com.devsan.santiniketanmess.models

data class Member(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val education: String = "",
    val photoUrl: String = "",
    val totalMeals: Int = 0,
    val totalDeposit: Double = 0.0
)
