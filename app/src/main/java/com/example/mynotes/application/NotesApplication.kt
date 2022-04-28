package com.example.mynotes.application

import android.app.Application
import com.example.mynotes.model.daos.NotesDao
import com.example.mynotes.model.daos.NotesRepository
import com.example.mynotes.model.daos.NotesRoom

class NotesApplication: Application() {

        private val database by lazy { NotesRoom.getDatabase((this@NotesApplication)) }
        val repository by lazy { NotesRepository(database.notesDao()) }
}