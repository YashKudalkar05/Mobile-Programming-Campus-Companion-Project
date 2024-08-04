package com.example.project

//import StudyGroupsActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.project.StudyGroupsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eventsButton: Button = findViewById(R.id.events_button)
        val studyGroupsButton: Button = findViewById(R.id.study_groups_button)
        val navigationButton: Button = findViewById(R.id.navigation_button)

        eventsButton.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }

        studyGroupsButton.setOnClickListener {
            val intent = Intent(this, StudyGroupsActivity::class.java)
            startActivity(intent)
        }

        navigationButton.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)

        }
    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
