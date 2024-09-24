package com.devsan.santiniketanmess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsan.santiniketanmess.databinding.ItemMemberBinding
import com.devsan.santiniketanmess.models.Member

class MembersAdapter(private val membersList: List<Member>) :
    RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = membersList[position]
        holder.bind(member)
    }

    override fun getItemCount(): Int {
        return membersList.size
    }

    class MemberViewHolder(private val binding: ItemMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.tvName.text = member.name
            binding.tvPhone.text = member.phone
            binding.tvEducation.text = member.education

            Glide.with(binding.root.context)
                .load(member.photoUrl)
                .into(binding.ivPhoto)
        }
    }
}