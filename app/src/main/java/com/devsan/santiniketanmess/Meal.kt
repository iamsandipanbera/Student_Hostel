package com.devsan.santiniketanmess

data class Meal(
    val member: String = "",
    val meals: Int = 0,
    val date: Long = System.currentTimeMillis()
)
