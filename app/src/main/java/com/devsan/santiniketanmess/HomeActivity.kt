package com.devsan.santiniketanmess

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Logout Button
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Clear the login status from SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear() // Clears all saved data
        editor.apply()

        // Sign out from Firebase and Google
        auth.signOut()
        googleSignInClient.signOut()

        // Redirect to MainActivity for login
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Close current activity
    }
}
