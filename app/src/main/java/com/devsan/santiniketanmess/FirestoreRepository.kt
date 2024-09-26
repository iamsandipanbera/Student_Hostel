package com.devsan.santiniketanmess.repository

import com.devsan.santiniketanmess.models.Expense
import com.devsan.santiniketanmess.models.Member
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // Add a new member
    suspend fun addMember(member: Member) {
        db.collection("members").document(member.id).set(member).await()
    }

    // Fetch all members
    suspend fun getAllMembers(): List<Member> {
        val result = db.collection("members").get().await()
        return result.toObjects(Member::class.java)
    }

    // Add or update monthly expenses
    suspend fun addOrUpdateExpenses(expense: Expense) {
        db.collection("expenses").document(expense.month).set(expense).await()
    }

    // Fetch monthly expenses
    suspend fun getMonthlyExpenses(month: String): Expense? {
        val result = db.collection("expenses").document(month).get().await()
        return result.toObject(Expense::class.java)
    }

    // Add deposits
    suspend fun addDeposit(memberId: String, amount: Double) {
        val memberDoc = db.collection("members").document(memberId)
        db.runTransaction { transaction ->
            val member = transaction.get(memberDoc).toObject(Member::class.java)
            if (member != null) {
                val newDeposit = member.totalDeposit + amount
                transaction.update(memberDoc, "totalDeposit", newDeposit)
            }
        }.await()
    }

    // Get member's deposit history
    suspend fun getMemberDepositHistory(memberId: String): List<Double> {
        val result =
            db.collection("members").document(memberId).collection("deposits").get().await()
        return result.documents.map { it.getDouble("amount") ?: 0.0 }
    }
}
