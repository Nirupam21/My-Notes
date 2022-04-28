package com.example.mynotes.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.mynotes.R
import com.example.mynotes.application.NotesApplication
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.databinding.ActivityShowNotesBinding
import com.example.mynotes.model.entities.NotesEntity
import com.example.mynotes.viewmodel.NotesModelFactory
import com.example.mynotes.viewmodel.NotesViewModel
import androidx.annotation.NonNull




class ShowNotesActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityShowNotesBinding

    private val mNotesViewModel: NotesViewModel by viewModels {
        NotesModelFactory((this.application as NotesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityShowNotesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val actionBar = supportActionBar

        if (actionBar != null) {

            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        showNotes()

        mBinding.ivNoteImage.setOnClickListener {

        }
    }

    private fun showNotes() {
            val note = intent.getParcelableExtra<NotesEntity>("Note")
        if (note != null) {
            Glide.with(this)
                .load(note.image)
                .into(mBinding.ivNoteImage)
            mBinding.etTitle.setText(note.title)
            mBinding.etLink.setText(note.link)
            mBinding.etExperiment.setText(note.experiment)
            mBinding.etComEnergy.setText(note.com_energy)
            mBinding.etFinalState.setText(note.final_state)
            mBinding.etLuminosity.setText(note.luminosity)
            mBinding.etTags.setText(note.tags)
            mBinding.etRemarks.setText(note.remarks)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}