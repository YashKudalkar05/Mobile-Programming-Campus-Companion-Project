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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class StudyGroupsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: StudyGroupAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var studentId: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_groups)


        // Setup bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem)
            true
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val user = auth.currentUser
        user?.let {
            // Fetch user details from Firestore
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: "N/A"
                        studentId = document.getString("student_id") ?: "N/A"
                        initializeUI()
                    } else {
                        Toast.makeText(this, "No such user found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun initializeUI() {
        val createGroupButton: Button = findViewById(R.id.createGroupButton)
        val recyclerView: RecyclerView = findViewById(R.id.groupsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudyGroupAdapter(studentId, name) { group, join ->
            updateGroupMembership(group, join)
        }
        recyclerView.adapter = adapter

        createGroupButton.setOnClickListener {

            createStudyGroup()
        }

        updateGroupsList()
    }

    private fun createStudyGroup() {
        val groupNameEditText: EditText = findViewById(R.id.groupNameEditText)
        val subjectEditText: EditText = findViewById(R.id.groupSubjectEditText)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)

        val groupname = groupNameEditText.text.toString()
        val subject = subjectEditText.text.toString()
        val description = descriptionEditText.text.toString()

        //val currentStudentID = studentId
        val currentUserName = name

        val studyGroup = StudyGroupAdapter.StudyGroup(
            name = groupname,
            subject = subject,
            description = description,
            members = listOf(currentUserName)
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
        val groupRef = firestore.collection("studyGroups").document(group.groupID) // Use group ID

        firestore.runTransaction { transaction ->
            val updatedGroup = transaction.get(groupRef).toObject(StudyGroupAdapter.StudyGroup::class.java)
                ?: throw IllegalStateException("Group not found")

            val currentUserName = name ?: "Unknown User" // Assuming you have a function to get the student's name

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


    private fun handleMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                home()
                true
            }
            R.id.user_details -> {
                // Handle study groups navigation
                profile()
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
    private fun home() {;
        // Navigate back to login activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
    private fun profile() {;
        // Navigate back to login activity
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }
}
