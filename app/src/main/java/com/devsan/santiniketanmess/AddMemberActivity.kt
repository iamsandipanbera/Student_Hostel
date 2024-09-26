package com.devsan.santiniketanmess

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddMemberActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var educationEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var saveButton: Button
    private var profileImageUri: Uri? = null

    private lateinit var database: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        // Initialize UI components
        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        educationEditText = findViewById(R.id.editTextEducation)
        profileImageView = findViewById(R.id.imageViewProfile)
        uploadButton = findViewById(R.id.buttonUpload)
        saveButton = findViewById(R.id.buttonSave)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance().getReference("Members")
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Handle profile image upload
        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Handle member save button click
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val education = educationEditText.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty() && education.isNotEmpty() && profileImageUri != null) {
                val memberId = database.push().key ?: return@setOnClickListener
                uploadImageToFirebase(memberId, name, phone, education)
            } else {
                Toast.makeText(this, "All fields and image are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle image picking from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            profileImageUri = data?.data
            profileImageView.setImageURI(profileImageUri)
        }
    }

    // Handle image upload failure
private fun handleImageUploadFailure() {
    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
}

// Upload image to Firebase Storage and save member details to Realtime Database and Firestore
private fun uploadImageToFirebase(memberId: String, name: String, phone: String, education: String) {
    val ref = storage.reference.child("profile_images/$memberId")
    profileImageUri?.let { uri ->
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveMemberToDatabase(memberId, name, phone, education, downloadUri.toString())
                }
            }
            .addOnFailureListener { handleImageUploadFailure() }
    }
}

    // Save member details to Firebase Realtime Database and Firestore
    private fun saveMemberToDatabase(memberId: String, name: String, phone: String, education: String, profileImageUrl: String) {
        // Create a member object
        val member = hashMapOf(
            "memberId" to memberId,
            "name" to name,
            "phone" to phone,
            "education" to education,
            "profileImageUrl" to profileImageUrl
        )

        // Save to Firebase Realtime Database
        database.child(memberId).setValue(member)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Save to Firestore
                    firestore.collection("Members").document(memberId).set(member)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()
                            finish()  // Close the activity
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Failed to save member", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show()
            }
    }
}
