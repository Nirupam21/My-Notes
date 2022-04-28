package com.example.mynotes.model.daos

import android.provider.ContactsContract
import androidx.room.*
import com.example.mynotes.model.entities.NotesEntity
import kotlinx.coroutines.flow.Flow
import java.net.IDN

@Dao
interface NotesDao {

    @Insert
    suspend fun insertNotesDetails(notes: NotesEntity)

    @Delete
    suspend fun deleteNotesDetails(notes: NotesEntity)

    @Query("Select * from Notes_Table order by ID")
    fun getAllNotesList(): Flow<List<NotesEntity>>

    @Query("Select * from Notes_Table where title like :query or tags like :query or experiment like :query or link like :query or com_energy like :query or final_state like :query or luminosity like :query")
    fun searchDatabase(query: String): Flow<List<NotesEntity>>

}