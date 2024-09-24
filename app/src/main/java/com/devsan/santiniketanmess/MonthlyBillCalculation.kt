package com.devsan.santiniketanmess

import com.google.firebase.database.FirebaseDatabase

class MonthlyBillCalculation {

    private val databaseRef = FirebaseDatabase.getInstance().getReference("monthlyData")

    fun calculateMonthlyBill(memberId: String, callback: (Double, Double) -> Unit) {
        val currentMonth = "2024-09"

        databaseRef.child(currentMonth).get().addOnSuccessListener { dataSnapshot ->
            val totalMarketing = dataSnapshot.child("totalMarketing").getValue(Double::class.java) ?: 0.0
            val rice = dataSnapshot.child("rice").getValue(Double::class.java) ?: 0.0
            val gas = dataSnapshot.child("gas").getValue(Double::class.java) ?: 0.0
            val guestMeal = dataSnapshot.child("guestMeal").getValue(Double::class.java) ?: 0.0
            val cookCharge = dataSnapshot.child("cookCharge").getValue(Double::class.java) ?: 0.0
            val paper = dataSnapshot.child("paper").getValue(Double::class.java) ?: 0.0
            val others = dataSnapshot.child("others").getValue(Double::class.java) ?: 0.0
            val totalMeals = dataSnapshot.child("totalMeals").getValue(Int::class.java) ?: 1
            val totalMembers = dataSnapshot.child("totalMembers").getValue(Int::class.java) ?: 1

            val memberMeals = dataSnapshot.child("members/$memberId/meals").getValue(Int::class.java) ?: 0
            val memberDeposit = dataSnapshot.child("members/$memberId/totalDeposit").getValue(Double::class.java) ?: 0.0

            val mealRate = ((totalMarketing + rice + gas) - guestMeal) / totalMeals
            val totalCookCharge = cookCharge * totalMembers
            val establishmentCharge = (totalCookCharge + paper + others) / totalMembers
            val totalMonthlyBill = (memberMeals * mealRate) + establishmentCharge
            val monthlyDue = totalMonthlyBill - memberDeposit

            callback(totalMonthlyBill, monthlyDue)
        }.addOnFailureListener {
            callback(0.0, 0.0) // Handle failure
        }
    }
}
