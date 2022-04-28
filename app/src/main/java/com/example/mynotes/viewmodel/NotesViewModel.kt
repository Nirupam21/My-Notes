package com.example.mynotes.viewmodel

import androidx.lifecycle.*
import com.example.mynotes.model.daos.NotesRepository
import com.example.mynotes.model.entities.NotesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NotesViewModel (private val repository: NotesRepository): ViewModel() {

    fun insert(notes: NotesEntity) = viewModelScope.launch {
        repository.insertNotes(notes)
    }

    fun delete(notes: NotesEntity) = viewModelScope.launch {
        repository.deleteNotes(notes)
    }

    fun searchDatabase(query: String): LiveData<List<NotesEntity>> {
        return repository.searchDatabase(query).asLiveData()
    }

    val allNotesList: LiveData<List<NotesEntity>> = repository.notesList.asLiveData()


}

class NotesModelFactory(private val repository: NotesRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotesViewModel::class.java)){
            @Suppress("Unchecked Cast")
            return NotesViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}