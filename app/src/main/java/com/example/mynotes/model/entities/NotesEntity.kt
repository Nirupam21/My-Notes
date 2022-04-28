package com.example.mynotes.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Notes_Table")
data class NotesEntity (

        @ColumnInfo val image: String,
        @ColumnInfo(name = "image_source") val imageSource: String,
        @ColumnInfo val title: String,
        @ColumnInfo val link: String,
        @ColumnInfo val experiment: String,
        @ColumnInfo val com_energy: String,
        @ColumnInfo(name = "luminosity") val luminosity: String,
        @ColumnInfo(name = "final_state") val final_state: String,
        @ColumnInfo val tags: String,
        @ColumnInfo val remarks: String,
        //@ColumnInfo(name = "favorite_dish") val favoriteDish: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Int = 0

    ):Parcelable
