package com.example.project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firestore = FirebaseFirestore.getInstance()

        val studentIDEditText: EditText = findViewById(R.id.studentIDEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val studentID = studentIDEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (studentID.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Student ID and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateStudent(studentID, password)
        }
    }

    private fun authenticateStudent(studentID: String, password: String) {
        firestore.collection("Student")
            .whereEqualTo("studentID", studentID)
            .whereEqualTo("Password", password)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(this, "Invalid Student ID or Password", Toast.LENGTH_SHORT).show()
                } else {
                    val student = result.documents.first()
                    val studentName = student.getString("Name") ?: "Unknown User"

                    // Save student ID and name
                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("student_id", studentID)
                        putString("student_name", studentName)
                        apply()
                    }

                    // Navigate to MainActivity if authentication is successful
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finish LoginActivity to prevent going back to it
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
