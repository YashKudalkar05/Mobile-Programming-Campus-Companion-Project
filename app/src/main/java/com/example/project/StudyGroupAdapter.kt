package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudyGroupAdapter(
    private val studentID: String,
    private val studentName: String,
    private val onJoinLeaveClick: (StudyGroup, Boolean) -> Unit
) : RecyclerView.Adapter<StudyGroupAdapter.ViewHolder>() {

    data class StudyGroup(
        val groupID: String = "",
        val name: String = "",
        val subject: String = "",
        val description: String = "",
        val members: List<String> = emptyList()
    )

    private var studyGroups = listOf<StudyGroup>()

    fun submitList(groups: List<StudyGroup>) {
        studyGroups = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = studyGroups[position]
        holder.bind(group)
    }

    override fun getItemCount(): Int = studyGroups.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        private val subjectTextView: TextView = itemView.findViewById(R.id.groupSubjectTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.groupDescriptionTextView)
        private val membersTextView: TextView = itemView.findViewById(R.id.groupMembersTextView)
        private val joinButton: Button = itemView.findViewById(R.id.joinButton)

        fun bind(group: StudyGroup) {
            nameTextView.text = group.name
            subjectTextView.text = group.subject
            descriptionTextView.text = group.description

            val isMember = group.members.contains(studentName)

            if (isMember) {
                joinButton.text = "Leave"
                membersTextView.text = "Members: ${group.members.joinToString(", ")}"
            } else {
                joinButton.text = "Join"
                membersTextView.text = ""
            }

            joinButton.setOnClickListener {
                onJoinLeaveClick(group, !isMember)
            }
        }
    }
}
