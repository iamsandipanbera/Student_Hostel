package com.devsan.santiniketanmess.utils

import com.devsan.santiniketanmess.models.Member
import com.devsan.santiniketanmess.models.Expense

class CalculationHelper {

    // Calculate Meal Rate
    fun calculateMealRate(totalMarketing: Double, rice: Double, gas: Double, guestMeal: Double, totalMeals: Int): Double {
        return if (totalMeals > 0) {
            (totalMarketing + rice + gas - guestMeal) / totalMeals
        } else {
            0.0
        }
    }

    // Calculate Establishment Charge
    fun calculateEstablishmentCharge(cookCharge: Double, paperCharge: Double, otherCharge: Double, totalMembers: Int): Double {
        return if (totalMembers > 0) {
            (cookCharge + paperCharge + otherCharge) / totalMembers
        } else {
            0.0
        }
    }

    // Calculate Total Monthly Bill for an Individual Member
    fun calculateTotalMonthlyBill(memberMeals: Int, mealRate: Double, establishmentCharge: Double): Double {
        return (memberMeals * mealRate) + establishmentCharge
    }

    // Calculate Monthly Due for an Individual Member
    fun calculateMonthlyDue(totalMonthlyBill: Double, totalDeposit: Double): Double {
        return totalMonthlyBill - totalDeposit
    }

}
