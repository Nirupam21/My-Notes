package com.example.mynotes.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynotes.R
import com.example.mynotes.databinding.ItemDishLayoutBinding
import com.example.mynotes.model.entities.NotesEntity
import com.example.mynotes.viewmodel.NotesViewModel

class NotesAdapter(private val context: Context, private val listener: INotesAdapter): RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

            private var notes: List<NotesEntity> = listOf()

            class ViewHolder(view: View): RecyclerView.ViewHolder(view){
                val ivNotesImage: ImageView = view.findViewById(R.id.iv_dish_image)
                val tvNotes: TextView = view.findViewById(R.id.tv_dish_title)
                val deletebtn: ImageView = view.findViewById(R.id.deletebtn)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(context),parent,false)*/

        val viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dish_layout,parent,false))

        viewHolder.deletebtn.setOnClickListener {
                listener.onItemClicked(notes[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        Glide.with(context)
            .load(note.image)
            .into(holder.ivNotesImage)

        holder.tvNotes.text = note.title

        holder.itemView.setOnClickListener {
                listener.onTitleClicked(note)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notesList(list: List<NotesEntity>) {
        notes = list
        notifyDataSetChanged()
    }
}

interface INotesAdapter {
    fun onItemClicked(note: NotesEntity)
    fun onTitleClicked(note: NotesEntity)
}