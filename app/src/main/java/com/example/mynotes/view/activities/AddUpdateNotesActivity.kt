package com.example.mynotes.view.activities

import android.Manifest
import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mynotes.R
import com.example.mynotes.application.NotesApplication
import com.example.mynotes.databinding.ActivityAddUpdateNotesBinding
import com.example.mynotes.databinding.DialogCustomImageSelectionBinding
import com.example.mynotes.databinding.DialogCustomListBinding
import com.example.mynotes.model.entities.NotesEntity
import com.example.mynotes.utils.Constants
import com.example.mynotes.view.adapter.CustomListAdapter
import com.example.mynotes.viewmodel.NotesModelFactory
import com.example.mynotes.viewmodel.NotesViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class AddUpdateNotesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateNotesBinding
    private var mImagePath: String = ""
    private lateinit var mCustomListDialog: Dialog

    private val mNotesViewModel:NotesViewModel by viewModels {
        NotesModelFactory((application as NotesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateNotesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val actionBar = supportActionBar

        if (actionBar != null) {

            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        mBinding.ivAddNoteImage.setOnClickListener(this@AddUpdateNotesActivity)
        mBinding.etLink.setOnClickListener(this)
        mBinding.etExperiment.setOnClickListener(this)
        mBinding.etLuminosity.setOnClickListener(this)
        mBinding.btnAddNote.setOnClickListener(this)
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

    override fun onClick(v: View) {

        when (v.id) {

            R.id.iv_add_note_image -> {

                customImageSelectionDialog()
                return
            }

            /*R.id.et_link -> {

                customItemsDialog(resources.getString(R.string.title_select_note_link),
                    Constants.noteLink(),
                    Constants.NOTE_LINK)
                return
            }*/
            R.id.et_experiment -> {
                customItemsDialog(resources.getString(R.string.title_select_note_experiment),
                    Constants.noteExperiment(),
                    Constants.NOTE_EXPERIMENT)
                return
            }
            /*R.id.et_luminosity -> {
                customItemsDialog(resources.getString(R.string.title_select_note_luminosity),
                    Constants.noteLuminosity(),
                    Constants.NOTE_LUMINOSITY)
                return
            }*/

            R.id.btn_add_note -> {
                val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                val link = mBinding.etLink.text.toString().trim { it <= ' ' }
                val experiment = mBinding.etExperiment2.text.toString().trim { it <= ' ' }
                val com_energy = mBinding.etComEnergy.text.toString().trim { it <= ' ' }
                val luminosity = mBinding.etLuminosity.text.toString().trim { it <= ' ' }
                val final_state = mBinding.etFinalState.text.toString().trim { it <= ' ' }
                val tags = mBinding.etTags.text.toString().trim { it <= ' '}
                val remarks = mBinding.etRemarks.text.toString().trim { it <= ' '}
                when {

                    TextUtils.isEmpty(title) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_title),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(link) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_link),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(experiment) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_experiment),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(luminosity) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_luminosity),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(com_energy) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_com_energy),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(tags) -> {
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            resources.getString(R.string.err_select_note_tags),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val notesDetails: NotesEntity = NotesEntity(
                            mImagePath,
                            Constants.DISH_IMAGE_SOURCE_LOCAL,
                            title,
                            link,
                            experiment,
                            com_energy,
                            luminosity,
                            final_state,
                            tags,
                            remarks
                        )

                        mNotesViewModel.insert(notesDetails)
                        Toast.makeText(
                            this@AddUpdateNotesActivity,
                            "You successfull added your favorite dish items",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Insertion","Success")
                        finish()
                    }

                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {

                // TODO Step 4: Get Image from Camera and set it to the ImageView
                // START
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap // Bitmap from camera
                //mBinding.ivDishImage.setImageBitmap(thumbnail)

                Glide.with(this)
                    .load(thumbnail)
                    .centerCrop()
                    .into(mBinding.ivNoteImage)

                mImagePath = saveImagetoInternalStorage(thumbnail)
                Log.i("ImagePath_camera",mImagePath)

                // TODO Step 6: Replace the add image icon with edit icon.
                // START
                // Replace the add icon with edit icon once the image is selected.
                mBinding.ivAddNoteImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddUpdateNotesActivity,
                        R.drawable.ic_edit
                    )
                )
                // END
            }
            if (requestCode == GALLERY) {
                data?.let {

                    val selectedPhotoUri = data.data

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error Loading Image", e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImagetoInternalStorage(bitmap)
                                    Log.i("ImagePath_gallery ",mImagePath)
                                }
                                return false
                            }

                        })
                        .into(mBinding.ivNoteImage)

                    // TODO Step 6: Replace the add image icon with edit icon.
                    // START
                    // Replace the add icon with edit icon once the image is selected.
                    mBinding.ivAddNoteImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@AddUpdateNotesActivity,
                            R.drawable.ic_edit
                        )
                    )
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this@AddUpdateNotesActivity)

        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this@AddUpdateNotesActivity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        // Here after all the permission are granted launch the CAMERA to capture an image.
                        if (report!!.areAllPermissionsGranted()) {

                            // TODO Step 2: Start camera using the Image capture action. Get the result in the onActivityResult method as we are using startActivityForResult.
                            // START
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                            // END
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {

            Dexter.withContext(this@AddUpdateNotesActivity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()
            dialog.dismiss()
        }

        //Start the dialog and display it on screen.
        dialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when(selection) {
            /*Constants.NOTE_LINK -> {
                mCustomListDialog.dismiss()
                mBinding.etLink.setText(item)
            }*/

            Constants.NOTE_EXPERIMENT -> {
                mCustomListDialog.dismiss()
                mBinding.etExperiment.setText(item)

                if(item != "others")
                    mBinding.etExperiment2.setText(item)

            }

           /* Constants.NOTE_LUMINOSITY -> {
                mCustomListDialog.dismiss()
                mBinding.etLuminosity.setText(item)
            }*/
        }
    }

    private fun showRationalDialogForPermissions() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImagetoInternalStorage(bitmap: Bitmap): String {

        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream)
            stream.flush()
            stream.close()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun customItemsDialog(title: String, itemsList: List<String>, selection:String) {

        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListAdapter(this,itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()

    }

    // TODO Step 1: Define the Companion Object to define the constants used in the class. We will define the constant for camera.
    // START
    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "NotesImages"
    }
}
