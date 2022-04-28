package com.example.mynotes.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.R
import com.example.mynotes.application.NotesApplication
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.model.entities.NotesEntity
import com.example.mynotes.view.adapter.INotesAdapter
import com.example.mynotes.view.adapter.NotesAdapter
import com.example.mynotes.viewmodel.NotesModelFactory
import com.example.mynotes.viewmodel.NotesViewModel
import androidx.appcompat.widget.SearchView


class MainActivity : AppCompatActivity(), INotesAdapter,SearchView.OnQueryTextListener {

    private lateinit var mBinding: ActivityMainBinding

    private val mNotesViewModel: NotesViewModel by viewModels {
            NotesModelFactory((this.application as NotesApplication).repository)
    }

    private val mAdapter: NotesAdapter by lazy {
            NotesAdapter(this,this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.floatingbtn.setOnClickListener {
               startActivity(Intent(this,AddUpdateNotesActivity::class.java))
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        mBinding.rvNotesList.layoutManager = GridLayoutManager(this,2)

        mBinding.rvNotesList.adapter = mAdapter

        mNotesViewModel.allNotesList.observe(this) {
                notes ->
                    notes.let {
                        if (it.isNotEmpty()) {
                            mBinding.rvNotesList.visibility = View.VISIBLE
                            mBinding.tvNoDishesAddedYet.visibility = View.GONE

                            mAdapter.notesList(it)
                        } else {
                            mBinding.rvNotesList.visibility = View.GONE
                            mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                        }
                    }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

            if(query != null){
                searchDatabase(query)
            }

           return true
    }

    override fun onQueryTextChange(query: String?): Boolean {

        if(query != null){
            searchDatabase(query)
        }

        return true
    }

    private fun searchDatabase(query: String) {

            val searchQuery = "%$query%"

            mNotesViewModel.searchDatabase(searchQuery).observe(this) {
                  notes->
                    notes.let {
                            mAdapter.notesList(it)
                    }
            }
    }

    override fun onItemClicked(note: NotesEntity) {
        mNotesViewModel.delete(note)
    }

    override fun onTitleClicked(note: NotesEntity) {
        startActivity(Intent(this,ShowNotesActivity::class.java)
            .putExtra("Note",note))

    }

}