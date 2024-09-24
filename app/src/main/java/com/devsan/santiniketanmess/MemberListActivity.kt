package com.devsan.santiniketanmess

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devsan.santiniketanmess.models.Member
import com.google.firebase.database.*

class MembersListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var membersAdapter: MembersAdapter
    private lateinit var database: DatabaseReference
    private val membersList = mutableListOf<Member>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        membersAdapter = MembersAdapter(membersList)
        recyclerView.adapter = membersAdapter

        // Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("members")

        // Fetch members data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                membersList.clear()
                for (dataSnapshot in snapshot.children) {
                    val member = dataSnapshot.getValue(Member::class.java)
                    if (member != null) {
                        membersList.add(member)
                    }
                }
                membersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
