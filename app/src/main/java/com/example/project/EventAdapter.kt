package com.example.project

// EventAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R

data class Event(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = ""
)

class EventAdapter : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)

        fun bind(event: Event) {
            titleTextView.text = event.title
            descriptionTextView.text = event.description
            dateTextView.text = event.date
            timeTextView.text = event.time
            locationTextView.text = event.location
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
