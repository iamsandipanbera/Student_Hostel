package com.devsan.santiniketanmess

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DepositAdapter(private val deposits: MutableList<Deposit>) : RecyclerView.Adapter<DepositAdapter.DepositViewHolder>() {

    fun addDeposit(deposit: Deposit) {
        deposits.add(deposit)
        notifyItemInserted(deposits.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deposit, parent, false)
        return DepositViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        val deposit = deposits[position]
        holder.amountTextView.text = "₹${deposit.amount}"
        holder.dateTextView.text = deposit.date
    }

    override fun getItemCount(): Int {
        return deposits.size
    }

    class DepositViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountTextView: TextView = itemView.findViewById(R.id.depositAmountTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.depositDateTextView)
    }
}
