package com.example.mynotes.model.daos

import android.content.Context
import android.provider.ContactsContract
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.mynotes.model.entities.NotesEntity


@Database(entities = [NotesEntity::class], version = 3)
abstract class NotesRoom: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {

        @Volatile
        private var INSTANCE: NotesRoom? = null

        fun getDatabase(context: Context): NotesRoom {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesRoom::class.java,
                    "notes_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }

        }

    }

}