package com.example.mynotes.model.daos

import androidx.annotation.WorkerThread
import com.example.mynotes.model.entities.NotesEntity
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {

    @WorkerThread
    suspend fun insertNotes(notesEntity: NotesEntity) {
        notesDao.insertNotesDetails(notesEntity)
    }

    suspend fun deleteNotes(notesEntity: NotesEntity){
        notesDao.deleteNotesDetails(notesEntity)
    }

    fun searchDatabase(query: String): Flow<List<NotesEntity>>{
            return notesDao.searchDatabase(query)
    }

    val notesList: Flow<List<NotesEntity>> = notesDao.getAllNotesList()

}