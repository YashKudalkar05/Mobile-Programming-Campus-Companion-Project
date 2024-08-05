package com.example.project

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class StudyGroupsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: StudyGroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_groups)

        // Initialize Toolbar
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the back button

        // Setup bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }


        firestore = FirebaseFirestore.getInstance()

        // Retrieve student ID and name from SharedPreferences
        val studentID = getStudentID() ?: "Unknown ID"
        val studentName = getStudentName() ?: "Unknown User"

        // Initialize UI components
        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val recyclerView: RecyclerView = findViewById(R.id.groupsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudyGroupAdapter(studentID, studentName) { group, join ->
            updateGroupMembership(group, join)
        }
        recyclerView.adapter = adapter

        createGroupButton.setOnClickListener {
            // Handle creating a new study group
            createStudyGroup()
        }
//        val toolbar1: Toolbar = findViewById(R.id.toolbar) // Added Toolbar setup
//        setSupportActionBar(toolbar1)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar1.setNavigationOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
        // Fetch and display study groups from Firebase
        updateGroupsList()
    }

    private fun createStudyGroup() {
        val groupNameEditText: EditText = findViewById(R.id.groupNameEditText)
        val subjectEditText: EditText = findViewById(R.id.groupSubjectEditText)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)

        val name = groupNameEditText.text.toString()
        val subject = subjectEditText.text.toString()
        val description = descriptionEditText.text.toString()

        val currentStudentID = getStudentID() ?: return
        val currentUserName = getStudentName() ?: "Unknown User"

        val studyGroup = StudyGroupAdapter.StudyGroup(
            name = name,
            subject = subject,
            description = description,
            members = listOf(currentUserName) // Add the current user to the group
        )

        firestore.collection("studyGroups")
            .add(studyGroup)
            .addOnSuccessListener { documentReference ->
                val newGroup = studyGroup.copy(groupID = documentReference.id)
                Toast.makeText(this, "Study group created successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                updateGroupsList()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create study group", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        findViewById<EditText>(R.id.groupNameEditText).text.clear()
        findViewById<EditText>(R.id.descriptionEditText).text.clear()
        findViewById<EditText>(R.id.groupSubjectEditText).text.clear()
    }

    private fun updateGroupsList() {
        firestore.collection("studyGroups")
            .get()
            .addOnSuccessListener { snapshot ->
                val groups = snapshot.documents.mapNotNull { document ->
                    document.toObject(StudyGroupAdapter.StudyGroup::class.java)?.copy(groupID = document.id)
                }
                adapter.submitList(groups)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update groups: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateGroupMembership(group: StudyGroupAdapter.StudyGroup, join: Boolean) {
        val currentStudentID = getStudentID() ?: return // Retrieve the current student ID
        val groupRef = firestore.collection("studyGroups").document(group.groupID) // Use group ID

        firestore.runTransaction { transaction ->
            val updatedGroup = transaction.get(groupRef).toObject(StudyGroupAdapter.StudyGroup::class.java)
                ?: throw IllegalStateException("Group not found")

            val currentUserName = getStudentName() ?: "Unknown User" // Assuming you have a function to get the student's name

            val updatedMembers = if (join) {
                updatedGroup.members.toMutableList().apply { add(currentUserName) }
            } else {
                updatedGroup.members.toMutableList().apply { remove(currentUserName) }
            }

            Log.d("UpdateMembership", "Group: ${group.name}, Members: $updatedMembers")

            transaction.set(groupRef, updatedGroup.copy(members = updatedMembers))

            // If no members left, delete the group
            if (updatedMembers.isEmpty()) {
                transaction.delete(groupRef)
            }

        }.addOnSuccessListener {
            val action = if (join) "joined" else "left"
            Toast.makeText(this, "Successfully $action the group", Toast.LENGTH_SHORT).show()
            updateGroupsList() // Refresh the list to reflect changes
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to update group membership: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getStudentID(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("student_id", null)
    }

    private fun getStudentName(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("student_name", "Unknown User")
    }



    private fun handleMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                // Handle event navigation
                true
            }
            R.id.user_details -> {
                // Handle study groups navigation
                true
            }
            R.id.logout -> {
                logout()
                true
            }
            else -> false
        }
    }
    private fun logout() {;
        val auth = FirebaseAuth.getInstance()

        auth.signOut()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
}
