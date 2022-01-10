package com.siddhantlad.hariyaalibazaarkotlin.submissioncriteria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.siddhantlad.hariyaalibazaarkotlin.R

class NoteAdapter(private val onItemClickListener: (Note) -> Unit)
    : ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent,
            false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)) {
            holder.tvTitle.text = title
            holder.tvDescription.setText(description)
            holder.tvPriority.text = priority.toString()
        }
    }

    fun getNoteAt(position: Int) = getItem(position)


    inner class NoteHolder(iv: View) : RecyclerView.ViewHolder(iv) {

        val tvTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val tvDescription: EditText = itemView.findViewById(R.id.itemQuanEt)
        val tvPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {
            itemView.setOnClickListener {
                if(adapterPosition != NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }

    }
}

private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.priority == newItem.priority
}